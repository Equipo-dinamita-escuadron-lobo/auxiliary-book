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
 * - criteria.fromRange / toRange → vía tabla "account" (CATALOGUE)
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
        // Routing RESTORE / BACKUP / DUPLICATE
        if (request.getDatosImportados() != null) {
            return ejecutarImportacion(request);
        }
        if (request.getEntDestino() == null || request.getEntDestino().isBlank()) {
            return ejecutarExportacion(request);
        }
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

        // Construir índices de equivalencias
        Map<Long, Long> catalogueIndex = construirIndiceLong(request.getEquivalenciasPrev(), "account");
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
     * - fromRange / toRange → tabla "account" (CATALOGUE)
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

    // ----------------------------------------------------------------
    // Modo BACKUP — exportar datos de la empresa origen
    // ----------------------------------------------------------------

    private CopyPhaseResponseDto ejecutarExportacion(CopyPhaseRequestDto request) {
        log.info("Modo BACKUP — exportando datos de la empresa origen: {}", request.getEntOrigen());

        try {
            List<AuxiliaryBookEntity> libros = sourceRepo.findAuxBooksForCopy(
                    request.getEntOrigen(), request.getSnapshotCorte());

            List<ScheduledReportEntity> reports = sourceRepo.findScheduledReportsForCopy(
                    request.getEntOrigen(), request.getSnapshotCorte());

            // Serializar libros auxiliares
            List<Map<String, Object>> librosData = new ArrayList<>();
            for (AuxiliaryBookEntity libro : libros) {
                Map<String, Object> libroMap = new LinkedHashMap<>();
                libroMap.put("id", libro.getId());
                libroMap.put("publicId", libro.getPublicId());
                libroMap.put("type", libro.getType() != null ? libro.getType().name() : null);
                libroMap.put("entId", libro.getEntId());
                libroMap.put("userId", libro.getUserId());
                libroMap.put("format", libro.getFormat() != null ? libro.getFormat().name() : null);
                libroMap.put("createdAt", libro.getCreatedAt() != null ? libro.getCreatedAt().toString() : null);

                // Template
                if (libro.getTemplate() != null) {
                    AuxiliaryBookTemplateEntity tpl = libro.getTemplate();
                    Map<String, Object> tplMap = new LinkedHashMap<>();
                    tplMap.put("id", tpl.getId());
                    tplMap.put("name", tpl.getName());
                    tplMap.put("pathLogotype", tpl.getPathLogotype());
                    tplMap.put("aligment", tpl.getAligment() != null ? tpl.getAligment().name() : null);
                    tplMap.put("font", tpl.getFont());
                    tplMap.put("mainColor", tpl.getMainColor());
                    libroMap.put("template", tplMap);
                } else {
                    libroMap.put("template", null);
                }

                // Criteria
                if (libro.getCriteria() != null) {
                    AuxiliaryBookCriteriaEntity crit = libro.getCriteria();
                    Map<String, Object> critMap = new LinkedHashMap<>();
                    critMap.put("id", crit.getId());
                    critMap.put("criteriaType", crit.getCriteriaType() != null ? crit.getCriteriaType().name() : null);
                    critMap.put("fromRange", crit.getFromRange());
                    critMap.put("toRange", crit.getToRange());
                    critMap.put("thirdPartyId", crit.getThirdPartyId());
                    critMap.put("costCenterId", crit.getCostCenterId());
                    critMap.put("startDate", crit.getStartDate() != null ? crit.getStartDate().toString() : null);
                    critMap.put("endDate", crit.getEndDate() != null ? crit.getEndDate().toString() : null);
                    libroMap.put("criteria", critMap);
                } else {
                    libroMap.put("criteria", null);
                }

                librosData.add(libroMap);
            }

            // Serializar reportes programados
            List<Map<String, Object>> reportsData = new ArrayList<>();
            for (ScheduledReportEntity report : reports) {
                Map<String, Object> repMap = new LinkedHashMap<>();
                repMap.put("id", report.getId());
                repMap.put("publicId", report.getPublicId());
                repMap.put("bookType", report.getBookType() != null ? report.getBookType().name() : null);
                repMap.put("frequency", report.getFrequency() != null ? report.getFrequency().name() : null);
                repMap.put("startAt", report.getStartAt() != null ? report.getStartAt().toString() : null);
                repMap.put("endAt", report.getEndAt() != null ? report.getEndAt().toString() : null);
                repMap.put("nextRunAt", report.getNextRunAt() != null ? report.getNextRunAt().toString() : null);
                repMap.put("status", report.getStatus() != null ? report.getStatus().name() : null);
                repMap.put("entId", report.getEntId());
                repMap.put("userId", report.getUserId());
                repMap.put("ownerSub", report.getOwnerSub());
                repMap.put("createdBy", report.getCreatedBy());
                repMap.put("deliveryWay", report.getDeliveryWay() != null ? report.getDeliveryWay().name() : null);
                repMap.put("emailTo", report.getEmailTo());
                repMap.put("emailSubject", report.getEmailSubject());
                repMap.put("emailBody", report.getEmailBody());
                repMap.put("templateName", report.getTemplateName());
                repMap.put("templatePathLogotype", report.getTemplatePathLogotype());
                repMap.put("templateAlignment", report.getTemplateAlignment());
                repMap.put("templateFont", report.getTemplateFont());
                repMap.put("templateFontSize", report.getTemplateFontSize());
                repMap.put("templateMainColor", report.getTemplateMainColor());

                // Criteria del reporte
                if (report.getCriteria() != null) {
                    AuxiliaryBookCriteriaEntity crit = report.getCriteria();
                    Map<String, Object> critMap = new LinkedHashMap<>();
                    critMap.put("id", crit.getId());
                    critMap.put("criteriaType", crit.getCriteriaType() != null ? crit.getCriteriaType().name() : null);
                    critMap.put("fromRange", crit.getFromRange());
                    critMap.put("toRange", crit.getToRange());
                    critMap.put("thirdPartyId", crit.getThirdPartyId());
                    critMap.put("costCenterId", crit.getCostCenterId());
                    critMap.put("startDate", crit.getStartDate() != null ? crit.getStartDate().toString() : null);
                    critMap.put("endDate", crit.getEndDate() != null ? crit.getEndDate().toString() : null);
                    repMap.put("criteria", critMap);
                } else {
                    repMap.put("criteria", null);
                }

                reportsData.add(repMap);
            }

            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("auxBooks", librosData);
            payload.put("scheduledReports", reportsData);

            int totalRegistros = librosData.size() + reportsData.size();
            log.info("BACKUP auxbook completado — {} libros, {} reportes exportados",
                    librosData.size(), reportsData.size());

            return CopyPhaseResponseDto.builder()
                    .estado("COMPLETADO")
                    .registrosProcesados(totalRegistros)
                    .equivalenciasGeneradas(Collections.emptyList())
                    .mensaje("Modo BACKUP — datos exportados correctamente")
                    .advertencias(Collections.emptyList())
                    .datosExportados(payload)
                    .build();

        } catch (Exception e) {
            log.error("Error durante exportación BACKUP auxbook: {}", e.getMessage(), e);
            return CopyPhaseResponseDto.builder()
                    .estado("ERROR_REINTENTABLE")
                    .mensaje("Error en BACKUP: " + e.getMessage())
                    .equivalenciasGeneradas(Collections.emptyList())
                    .advertencias(Collections.emptyList())
                    .build();
        }
    }

    // ----------------------------------------------------------------
    // Modo RESTORE — importar datos en la empresa destino
    // ----------------------------------------------------------------

    @SuppressWarnings("unchecked")
    private CopyPhaseResponseDto ejecutarImportacion(CopyPhaseRequestDto request) {
        String idProceso = request.getIdProceso().toString();
        log.info("Modo RESTORE — importando datos en empresa destino: {} (proceso {})",
                request.getEntDestino(), idProceso);

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

        Map<Long, Long> catalogueIndex = construirIndiceLong(request.getEquivalenciasPrev(), "account");
        Map<String, String> thirdsIndex = construirIndiceString(request.getEquivalenciasPrev(), "third");

        List<String> advertencias = new ArrayList<>();
        List<CopyEquivalenciaDto> equivalencias = new ArrayList<>();
        int totalRegistros = 0;

        try {
            Map<String, Object> datos = (Map<String, Object>) request.getDatosImportados();

            // ---- Restaurar AuxiliaryBookEntity ----
            List<Map<String, Object>> librosData =
                    datos.containsKey("auxBooks") ? (List<Map<String, Object>>) datos.get("auxBooks") : Collections.emptyList();

            for (Map<String, Object> libroMap : librosData) {
                Long idOriginal = toLong(libroMap.get("id"));

                AuxiliaryBookEntity nuevo = new AuxiliaryBookEntity();
                nuevo.setId(null);
                nuevo.setPublicId(UUID.randomUUID().toString());
                nuevo.setEntId(request.getEntDestino());
                nuevo.setUserId(toLong(libroMap.get("userId")));

                String typeStr = toStr(libroMap.get("type"));
                if (typeStr != null) {
                    try {
                        nuevo.setType(com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookType.valueOf(typeStr));
                    } catch (IllegalArgumentException ex) {
                        advertencias.add("AuxBook idOriginal=" + idOriginal + ": type desconocido '" + typeStr + "', ignorado.");
                    }
                }

                String formatStr = toStr(libroMap.get("format"));
                if (formatStr != null) {
                    try {
                        nuevo.setFormat(com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookFormat.valueOf(formatStr));
                    } catch (IllegalArgumentException ex) {
                        advertencias.add("AuxBook idOriginal=" + idOriginal + ": format desconocido '" + formatStr + "', ignorado.");
                    }
                }

                // Template
                if (libroMap.get("template") instanceof Map<?, ?> tplRaw) {
                    Map<String, Object> tplMap = (Map<String, Object>) tplRaw;
                    AuxiliaryBookTemplateEntity tpl = new AuxiliaryBookTemplateEntity();
                    tpl.setId(null);
                    tpl.setName(toStr(tplMap.get("name")));
                    tpl.setPathLogotype(toStr(tplMap.get("pathLogotype")));
                    String aligStr = toStr(tplMap.get("aligment"));
                    if (aligStr != null) {
                        try {
                            tpl.setAligment(com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAlignment.valueOf(aligStr));
                        } catch (IllegalArgumentException ex) {
                            advertencias.add("AuxBook idOriginal=" + idOriginal + ": aligment desconocido '" + aligStr + "', ignorado.");
                        }
                    }
                    tpl.setFont(toStr(tplMap.get("font")));
                    tpl.setMainColor(toStr(tplMap.get("mainColor")));
                    nuevo.setTemplate(tpl);
                }

                // Criteria con remap de FKs cross-service
                if (libroMap.get("criteria") instanceof Map<?, ?> critRaw) {
                    Map<String, Object> critMap = (Map<String, Object>) critRaw;
                    AuxiliaryBookCriteriaEntity crit = new AuxiliaryBookCriteriaEntity();
                    crit.setId(null);
                    String critTypeStr = toStr(critMap.get("criteriaType"));
                    if (critTypeStr != null) {
                        try {
                            crit.setCriteriaType(com.unicauca.edu.co.auxiliary_book.domain.models.enums.ECriteriaType.valueOf(critTypeStr));
                        } catch (IllegalArgumentException ex) {
                            advertencias.add("AuxBook idOriginal=" + idOriginal + ": criteriaType desconocido '" + critTypeStr + "'.");
                        }
                    }

                    // Remap fromRange vía CATALOGUE
                    Long fromRangeOrig = toLong(critMap.get("fromRange"));
                    if (fromRangeOrig != null) {
                        Long fromRangeNew = catalogueIndex.get(fromRangeOrig);
                        if (fromRangeNew == null) {
                            advertencias.add("AuxBook idOriginal=" + idOriginal + " criteria: fromRange=" + fromRangeOrig + " sin equivalencia en CATALOGUE; conservado.");
                            crit.setFromRange(fromRangeOrig);
                        } else {
                            crit.setFromRange(fromRangeNew);
                        }
                    }

                    // Remap toRange vía CATALOGUE
                    Long toRangeOrig = toLong(critMap.get("toRange"));
                    if (toRangeOrig != null) {
                        Long toRangeNew = catalogueIndex.get(toRangeOrig);
                        crit.setToRange(toRangeNew != null ? toRangeNew : toRangeOrig);
                    }

                    // Remap thirdPartyId vía THIRDS
                    String thirdOrig = toStr(critMap.get("thirdPartyId"));
                    if (thirdOrig != null) {
                        String thirdNew = thirdsIndex.get(thirdOrig);
                        if (thirdNew == null) {
                            advertencias.add("AuxBook idOriginal=" + idOriginal + " criteria: thirdPartyId=" + thirdOrig + " sin equivalencia en THIRDS; conservado.");
                            crit.setThirdPartyId(thirdOrig);
                        } else {
                            crit.setThirdPartyId(thirdNew);
                        }
                    }

                    String startDateStr = toStr(critMap.get("startDate"));
                    if (startDateStr != null) crit.setStartDate(java.time.LocalDate.parse(startDateStr));
                    String endDateStr = toStr(critMap.get("endDate"));
                    if (endDateStr != null) crit.setEndDate(java.time.LocalDate.parse(endDateStr));
                    crit.setCostCenterId(toStr(critMap.get("costCenterId")));
                    nuevo.setCriteria(crit);
                }

                AuxiliaryBookEntity guardado = targetRepo.guardarAuxBook(nuevo);
                equivalencias.add(CopyEquivalenciaDto.builder()
                        .modulo(MODULO).tabla("auxbook")
                        .idViejo(idOriginal != null ? String.valueOf(idOriginal) : null)
                        .idNuevo(String.valueOf(guardado.getId()))
                        .build());
                totalRegistros++;
            }

            // ---- Restaurar ScheduledReportEntity ----
            List<Map<String, Object>> reportsData =
                    datos.containsKey("scheduledReports") ? (List<Map<String, Object>>) datos.get("scheduledReports") : Collections.emptyList();

            for (Map<String, Object> repMap : reportsData) {
                ScheduledReportEntity nuevo = new ScheduledReportEntity();
                nuevo.setId(null);
                nuevo.setPublicId(UUID.randomUUID().toString());
                nuevo.setEntId(request.getEntDestino());
                nuevo.setUserId(toLong(repMap.get("userId")));
                nuevo.setOwnerSub(toStr(repMap.get("ownerSub")));
                nuevo.setCreatedBy(toStr(repMap.get("createdBy")));

                String bookTypeStr = toStr(repMap.get("bookType"));
                if (bookTypeStr != null) {
                    try {
                        nuevo.setBookType(com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookType.valueOf(bookTypeStr));
                    } catch (IllegalArgumentException ex) {
                        advertencias.add("ScheduledReport: bookType desconocido '" + bookTypeStr + "', ignorado.");
                    }
                }

                String freqStr = toStr(repMap.get("frequency"));
                if (freqStr != null) {
                    try {
                        nuevo.setFrequency(com.unicauca.edu.co.auxiliary_book.domain.models.enums.EFrequency.valueOf(freqStr));
                    } catch (IllegalArgumentException ex) {
                        advertencias.add("ScheduledReport: frequency desconocida '" + freqStr + "', ignorada.");
                    }
                }

                String statusStr = toStr(repMap.get("status"));
                if (statusStr != null) {
                    try {
                        nuevo.setStatus(com.unicauca.edu.co.auxiliary_book.domain.models.enums.EJobStatus.valueOf(statusStr));
                    } catch (IllegalArgumentException ex) {
                        advertencias.add("ScheduledReport: status desconocido '" + statusStr + "', ignorado.");
                    }
                }

                String startAtStr = toStr(repMap.get("startAt"));
                if (startAtStr != null) nuevo.setStartAt(Instant.parse(startAtStr));
                String endAtStr = toStr(repMap.get("endAt"));
                if (endAtStr != null) nuevo.setEndAt(Instant.parse(endAtStr));
                String nextRunAtStr = toStr(repMap.get("nextRunAt"));
                if (nextRunAtStr != null) nuevo.setNextRunAt(Instant.parse(nextRunAtStr));

                String deliveryWayStr = toStr(repMap.get("deliveryWay"));
                if (deliveryWayStr != null) {
                    try {
                        nuevo.setDeliveryWay(com.unicauca.edu.co.auxiliary_book.domain.models.enums.EDeliveryWay.valueOf(deliveryWayStr));
                    } catch (IllegalArgumentException ex) {
                        advertencias.add("ScheduledReport: deliveryWay desconocida '" + deliveryWayStr + "', ignorada.");
                    }
                }

                nuevo.setEmailTo(toStr(repMap.get("emailTo")));
                nuevo.setEmailSubject(toStr(repMap.get("emailSubject")));
                nuevo.setEmailBody(toStr(repMap.get("emailBody")));
                nuevo.setTemplateName(toStr(repMap.get("templateName")));
                nuevo.setTemplatePathLogotype(toStr(repMap.get("templatePathLogotype")));
                nuevo.setTemplateAlignment(toStr(repMap.get("templateAlignment")));
                nuevo.setTemplateFont(toStr(repMap.get("templateFont")));
                Object fontSizeObj = repMap.get("templateFontSize");
                if (fontSizeObj instanceof Number n) nuevo.setTemplateFontSize(n.intValue());
                nuevo.setTemplateMainColor(toStr(repMap.get("templateMainColor")));

                // Criteria del reporte con remap de FKs cross-service
                if (repMap.get("criteria") instanceof Map<?, ?> critRaw) {
                    Map<String, Object> critMap = (Map<String, Object>) critRaw;
                    AuxiliaryBookCriteriaEntity crit = new AuxiliaryBookCriteriaEntity();
                    crit.setId(null);
                    String critTypeStr = toStr(critMap.get("criteriaType"));
                    if (critTypeStr != null) {
                        try {
                            crit.setCriteriaType(com.unicauca.edu.co.auxiliary_book.domain.models.enums.ECriteriaType.valueOf(critTypeStr));
                        } catch (IllegalArgumentException ex) {
                            advertencias.add("ScheduledReport criteria: criteriaType desconocido '" + critTypeStr + "'.");
                        }
                    }

                    // Remap fromRange vía CATALOGUE
                    Long fromRangeOrig = toLong(critMap.get("fromRange"));
                    if (fromRangeOrig != null) {
                        Long fromRangeNew = catalogueIndex.get(fromRangeOrig);
                        if (fromRangeNew == null) {
                            advertencias.add("ScheduledReport criteria: fromRange=" + fromRangeOrig + " sin equivalencia en CATALOGUE; conservado.");
                            crit.setFromRange(fromRangeOrig);
                        } else {
                            crit.setFromRange(fromRangeNew);
                        }
                    }

                    // Remap toRange vía CATALOGUE
                    Long toRangeOrig = toLong(critMap.get("toRange"));
                    if (toRangeOrig != null) {
                        Long toRangeNew = catalogueIndex.get(toRangeOrig);
                        crit.setToRange(toRangeNew != null ? toRangeNew : toRangeOrig);
                    }

                    // Remap thirdPartyId vía THIRDS
                    String thirdOrig = toStr(critMap.get("thirdPartyId"));
                    if (thirdOrig != null) {
                        String thirdNew = thirdsIndex.get(thirdOrig);
                        if (thirdNew == null) {
                            advertencias.add("ScheduledReport criteria: thirdPartyId=" + thirdOrig + " sin equivalencia en THIRDS; conservado.");
                            crit.setThirdPartyId(thirdOrig);
                        } else {
                            crit.setThirdPartyId(thirdNew);
                        }
                    }

                    crit.setCostCenterId(toStr(critMap.get("costCenterId")));
                    String startDateStr = toStr(critMap.get("startDate"));
                    if (startDateStr != null) crit.setStartDate(java.time.LocalDate.parse(startDateStr));
                    String endDateStr = toStr(critMap.get("endDate"));
                    if (endDateStr != null) crit.setEndDate(java.time.LocalDate.parse(endDateStr));
                    nuevo.setCriteria(crit);
                }

                targetRepo.guardarScheduledReport(nuevo);
                totalRegistros++;
            }

        } catch (Exception e) {
            log.error("Error inesperado durante importación RESTORE auxbook del proceso {}: {}", idProceso, e.getMessage(), e);
            registrarFallo(request, e.getMessage(), logInicio.getFechaInicio());
            return CopyPhaseResponseDto.builder()
                    .estado("ERROR_REINTENTABLE")
                    .mensaje("Error en RESTORE: " + e.getMessage())
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
                .mensaje("Modo RESTORE — datos importados en empresa " + request.getEntDestino())
                .advertencias(advertencias)
                .build();
    }

    // ----------------------------------------------------------------
    // Helpers de conversión de tipo
    // ----------------------------------------------------------------

    private Long toLong(Object v) {
        if (v == null) return null;
        if (v instanceof Long l) return l;
        if (v instanceof Integer i) return i.longValue();
        if (v instanceof Number n) return n.longValue();
        return null;
    }

    private String toStr(Object v) {
        return v != null ? v.toString() : null;
    }
}
