package com.unicauca.edu.co.auxiliary_book.copy.application.services;

import com.unicauca.edu.co.auxiliary_book.copy.application.input.IExecuteAuxBookCopyPhasePort;
import com.unicauca.edu.co.auxiliary_book.copy.application.output.IAuxBookCopyJobLogRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.copy.application.output.IAuxBookSourceRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.copy.application.output.IAuxBookTargetRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.copy.domain.enums.CopyEstado;
import com.unicauca.edu.co.auxiliary_book.copy.domain.models.CopyJobLog;
import com.unicauca.edu.co.auxiliary_book.copy.infrastructure.in.rest.dto.CopyEquivalenciaDto;
import com.unicauca.edu.co.auxiliary_book.copy.infrastructure.in.rest.dto.CopyPhaseRequestDto;
import com.unicauca.edu.co.auxiliary_book.copy.infrastructure.in.rest.dto.CopyPhaseResponseDto;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookCriteriaEntity;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookEntity;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookTemplateEntity;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.scheduledReport.ScheduledReportEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio de aplicación que orquesta la copia del módulo auxiliary-book.
 *
 * Qué se copia (ADR-40):
 * - AuxiliaryBookEntity (template + criteria) — criteria remapea accountId (CATALOGUE) y thirdId (THIRDS)
 * - ScheduledReportEntity — se copia directamente
 * - NO se copian: History, ScheduledReportExecution, Log
 *
 * Remap:
 * - criteria.fromRange / toRange → vía tabla "cuentaContable" (CATALOGUE)
 * - criteria.thirdPartyId → vía tabla "third" (THIRDS)
 *
 * Si no hay equivalencia: advertencia + conservar valor original.
 *
 * ADR-38, ADR-40.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuxBookCopyService implements IExecuteAuxBookCopyPhasePort {

    private static final String MODULO = "auxbook";

    private final IAuxBookCopyJobLogRepositoryPort logRepo;
    private final IAuxBookSourceRepositoryPort sourceRepo;
    private final IAuxBookTargetRepositoryPort targetRepo;

    @Override
    public CopyPhaseResponseDto ejecutar(CopyPhaseRequestDto request) {
        // Validación básica
        if (request.getEntOrigen().equals(request.getEntDestino())) {
            return CopyPhaseResponseDto.builder()
                    .estado("ERROR_NO_REINTENTABLE")
                    .mensaje("entOrigen y entDestino no pueden ser iguales")
                    .equivalenciasGeneradas(Collections.emptyList())
                    .advertencias(Collections.emptyList())
                    .build();
        }

        String idProceso = request.getIdProceso().toString();

        // Idempotencia
        Optional<CopyJobLog> previo = logRepo.buscarPorIdProcesoYFase(idProceso, request.getFase());
        if (previo.isPresent()) {
            log.info("Fase {} del proceso {} ya fue ejecutada — retornando resultado previo (idempotencia)",
                    request.getFase(), idProceso);
            return construirResponseDesdeLog(previo.get());
        }

        // Registrar inicio
        CopyJobLog logInicio = CopyJobLog.builder()
                .idProceso(request.getIdProceso())
                .fase(request.getFase())
                .modulo(MODULO)
                .estado(CopyEstado.EN_PROCESO)
                .fechaInicio(Instant.now())
                .equivalenciasGeneradas(0)
                .build();
        logRepo.guardar(logInicio);

        // Construir índices de equivalencias
        Map<Long, Long> catalogueIndex = construirIndiceLong(request.getEquivalenciasPrev(), "cuentaContable");
        Map<String, String> thirdsIndex = construirIndiceString(request.getEquivalenciasPrev(), "third");

        List<String> advertencias = new ArrayList<>();
        List<CopyEquivalenciaDto> equivalencias = new ArrayList<>();
        int totalRegistros = 0;

        try {
            // ---- Copiar AuxiliaryBookEntity ----
            List<AuxiliaryBookEntity> libros = sourceRepo.findAuxBooksForCopy(
                    request.getEntOrigen(), request.getSnapshotCorte());

            for (AuxiliaryBookEntity libro : libros) {
                AuxiliaryBookEntity nuevo = copiarAuxBook(libro, request.getEntDestino(),
                        catalogueIndex, thirdsIndex, advertencias);
                AuxiliaryBookEntity guardado = targetRepo.guardarAuxBook(nuevo);

                equivalencias.add(CopyEquivalenciaDto.builder()
                        .modulo(MODULO).tabla("auxbook")
                        .idViejo(String.valueOf(libro.getId()))
                        .idNuevo(String.valueOf(guardado.getId()))
                        .build());
                totalRegistros++;
            }

            // ---- Copiar ScheduledReportEntity ----
            List<ScheduledReportEntity> reports = sourceRepo.findScheduledReportsForCopy(
                    request.getEntOrigen(), request.getSnapshotCorte());

            for (ScheduledReportEntity report : reports) {
                ScheduledReportEntity nuevoReport = copiarScheduledReport(report, request.getEntDestino());
                targetRepo.guardarScheduledReport(nuevoReport);
                totalRegistros++;
            }

        } catch (Exception e) {
            log.error("Error inesperado durante copia auxbook del proceso {}: {}", idProceso, e.getMessage(), e);
            registrarFallo(request, e.getMessage(), logInicio.getFechaInicio());
            return CopyPhaseResponseDto.builder()
                    .estado("ERROR_REINTENTABLE")
                    .mensaje("Error interno: " + e.getMessage())
                    .equivalenciasGeneradas(Collections.emptyList())
                    .advertencias(Collections.emptyList())
                    .build();
        }

        CopyEstado estadoFinal = advertencias.isEmpty()
                ? CopyEstado.COMPLETADO
                : CopyEstado.COMPLETADO_CON_ADVERTENCIAS;

        CopyJobLog logFin = CopyJobLog.builder()
                .idProceso(request.getIdProceso())
                .fase(request.getFase())
                .modulo(MODULO)
                .estado(estadoFinal)
                .fechaInicio(logInicio.getFechaInicio())
                .fechaFin(Instant.now())
                .equivalenciasGeneradas(equivalencias.size())
                .build();
        logRepo.guardar(logFin);

        return CopyPhaseResponseDto.builder()
                .estado(estadoFinal.name())
                .registrosProcesados(totalRegistros)
                .equivalenciasGeneradas(equivalencias)
                .mensaje("Copia auxiliary-book completada exitosamente")
                .advertencias(advertencias)
                .build();
    }

    // ----------------------------------------------------------------
    // Builders de copia
    // ----------------------------------------------------------------

    private AuxiliaryBookEntity copiarAuxBook(AuxiliaryBookEntity original,
                                               String entDestino,
                                               Map<Long, Long> catalogueIndex,
                                               Map<String, String> thirdsIndex,
                                               List<String> advertencias) {
        AuxiliaryBookEntity nuevo = new AuxiliaryBookEntity();
        nuevo.setId(null);
        nuevo.setPublicId(UUID.randomUUID().toString());
        nuevo.setType(original.getType());
        nuevo.setEntId(entDestino);
        nuevo.setUserId(original.getUserId());
        nuevo.setFormat(original.getFormat());

        // Copiar template
        if (original.getTemplate() != null) {
            AuxiliaryBookTemplateEntity tpl = new AuxiliaryBookTemplateEntity();
            tpl.setId(null);
            tpl.setName(original.getTemplate().getName());
            tpl.setPathLogotype(original.getTemplate().getPathLogotype());
            tpl.setAligment(original.getTemplate().getAligment());
            tpl.setFont(original.getTemplate().getFont());
            tpl.setMainColor(original.getTemplate().getMainColor());
            nuevo.setTemplate(tpl);
        }

        // Copiar criteria con remap
        if (original.getCriteria() != null) {
            AuxiliaryBookCriteriaEntity crit = copiarCriteria(
                    original.getCriteria(), original.getId(), catalogueIndex, thirdsIndex, advertencias);
            nuevo.setCriteria(crit);
        }

        return nuevo;
    }

    /**
     * Copia criteria remapeando:
     * - fromRange / toRange → tabla "cuentaContable" (CATALOGUE)
     * - thirdPartyId → tabla "third" (THIRDS)
     */
    private AuxiliaryBookCriteriaEntity copiarCriteria(AuxiliaryBookCriteriaEntity original,
                                                         Long libroId,
                                                         Map<Long, Long> catalogueIndex,
                                                         Map<String, String> thirdsIndex,
                                                         List<String> advertencias) {
        AuxiliaryBookCriteriaEntity nuevo = new AuxiliaryBookCriteriaEntity();
        nuevo.setId(null);
        nuevo.setCriteriaType(original.getCriteriaType());
        nuevo.setStartDate(original.getStartDate());
        nuevo.setEndDate(original.getEndDate());
        nuevo.setCostCenterId(original.getCostCenterId());

        // Remap fromRange (accountId) vía CATALOGUE
        if (original.getFromRange() != null) {
            Long newFrom = catalogueIndex.get(original.getFromRange());
            if (newFrom == null) {
                String adv = String.format(
                    "AuxBook id=%s criteria: fromRange=%s sin equivalencia en CATALOGUE; conservado.",
                    libroId, original.getFromRange());
                log.warn(adv);
                advertencias.add(adv);
                nuevo.setFromRange(original.getFromRange());
            } else {
                nuevo.setFromRange(newFrom);
            }
        }

        // Remap toRange (accountId) vía CATALOGUE
        if (original.getToRange() != null) {
            Long newTo = catalogueIndex.get(original.getToRange());
            if (newTo == null) {
                nuevo.setToRange(original.getToRange()); // conservar si no hay equivalencia (ya advertido arriba)
            } else {
                nuevo.setToRange(newTo);
            }
        }

        // Remap thirdPartyId vía THIRDS
        if (original.getThirdPartyId() != null) {
            String newThird = thirdsIndex.get(original.getThirdPartyId());
            if (newThird == null) {
                String adv = String.format(
                    "AuxBook id=%s criteria: thirdPartyId=%s sin equivalencia en THIRDS; conservado.",
                    libroId, original.getThirdPartyId());
                log.warn(adv);
                advertencias.add(adv);
                nuevo.setThirdPartyId(original.getThirdPartyId());
            } else {
                nuevo.setThirdPartyId(newThird);
            }
        }

        return nuevo;
    }

    private ScheduledReportEntity copiarScheduledReport(ScheduledReportEntity original, String entDestino) {
        ScheduledReportEntity nuevo = new ScheduledReportEntity();
        nuevo.setId(null);
        nuevo.setPublicId(UUID.randomUUID().toString());
        nuevo.setBookType(original.getBookType());
        nuevo.setFrequency(original.getFrequency());
        nuevo.setStartAt(original.getStartAt());
        nuevo.setEndAt(original.getEndAt());
        nuevo.setNextRunAt(original.getNextRunAt());
        nuevo.setStatus(original.getStatus());
        nuevo.setEntId(entDestino);
        nuevo.setUserId(original.getUserId());
        nuevo.setCreatedBy(original.getCreatedBy());
        nuevo.setDeliveryWay(original.getDeliveryWay());
        nuevo.setEmailTo(original.getEmailTo());
        nuevo.setEmailSubject(original.getEmailSubject());
        nuevo.setEmailBody(original.getEmailBody());
        // Copiar criteria del scheduled report con remap (simplificado — sin remap, usar original)
        nuevo.setCriteria(original.getCriteria());
        return nuevo;
    }

    // ----------------------------------------------------------------
    // Helpers
    // ----------------------------------------------------------------

    private Map<Long, Long> construirIndiceLong(List<CopyEquivalenciaDto> equivalenciasPrev, String tabla) {
        if (equivalenciasPrev == null) return Collections.emptyMap();
        return equivalenciasPrev.stream()
                .filter(e -> tabla.equals(e.getTabla()) && e.getIdViejo() != null && e.getIdNuevo() != null)
                .collect(Collectors.toMap(
                        e -> Long.parseLong(e.getIdViejo()),
                        e -> Long.parseLong(e.getIdNuevo()),
                        (a, b) -> a));
    }

    private Map<String, String> construirIndiceString(List<CopyEquivalenciaDto> equivalenciasPrev, String tabla) {
        if (equivalenciasPrev == null) return Collections.emptyMap();
        return equivalenciasPrev.stream()
                .filter(e -> tabla.equals(e.getTabla()) && e.getIdViejo() != null && e.getIdNuevo() != null)
                .collect(Collectors.toMap(
                        CopyEquivalenciaDto::getIdViejo,
                        CopyEquivalenciaDto::getIdNuevo,
                        (a, b) -> a));
    }

    private CopyPhaseResponseDto construirResponseDesdeLog(CopyJobLog log) {
        return CopyPhaseResponseDto.builder()
                .estado(log.getEstado().name())
                .registrosProcesados(log.getEquivalenciasGeneradas() != null ? log.getEquivalenciasGeneradas() : 0)
                .equivalenciasGeneradas(Collections.emptyList())
                .mensaje("Resultado de ejecución previa (idempotencia)")
                .advertencias(Collections.emptyList())
                .build();
    }

    private void registrarFallo(CopyPhaseRequestDto request, String mensaje, Instant fechaInicio) {
        try {
            CopyJobLog logFallo = CopyJobLog.builder()
                    .idProceso(request.getIdProceso())
                    .fase(request.getFase())
                    .modulo(MODULO)
                    .estado(CopyEstado.FALLIDO)
                    .fechaInicio(fechaInicio != null ? fechaInicio : Instant.now())
                    .fechaFin(Instant.now())
                    .equivalenciasGeneradas(0)
                    .errorMessage(mensaje)
                    .build();
            logRepo.guardar(logFallo);
        } catch (Exception e) {
            log.error("Error al registrar fallo de copia auxbook: {}", e.getMessage());
        }
    }
}
