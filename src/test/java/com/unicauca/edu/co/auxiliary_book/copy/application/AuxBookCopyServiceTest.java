package com.unicauca.edu.co.auxiliary_book.copy.application;

import com.unicauca.edu.co.auxiliary_book.copy.application.output.IAuxBookCopyJobLogRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.copy.application.output.IAuxBookSourceRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.copy.application.output.IAuxBookTargetRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.copy.application.services.AuxBookCopyService;
import com.unicauca.edu.co.auxiliary_book.copy.domain.enums.CopyEstado;
import com.unicauca.edu.co.auxiliary_book.copy.domain.models.CopyJobLog;
import com.unicauca.edu.co.auxiliary_book.copy.infrastructure.in.rest.dto.CopyEquivalenciaDto;
import com.unicauca.edu.co.auxiliary_book.copy.infrastructure.in.rest.dto.CopyPhaseRequestDto;
import com.unicauca.edu.co.auxiliary_book.copy.infrastructure.in.rest.dto.CopyPhaseResponseDto;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookType;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookFormat;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.ECriteriaType;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookCriteriaEntity;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookEntity;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookTemplateEntity;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.scheduledReport.ScheduledReportEntity;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAlignment;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EFrequency;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EJobStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para AuxBookCopyService.
 * Mockito standalone — sin contexto Spring.
 * RED phase: Fase 9.1.
 */
@ExtendWith(MockitoExtension.class)
class AuxBookCopyServiceTest {

    @Mock private IAuxBookCopyJobLogRepositoryPort logRepo;
    @Mock private IAuxBookSourceRepositoryPort sourceRepo;
    @Mock private IAuxBookTargetRepositoryPort targetRepo;

    private AuxBookCopyService service;

    @BeforeEach
    void setUp() {
        service = new AuxBookCopyService(logRepo, sourceRepo, targetRepo);
    }

    // ----------------------------------------------------------------
    // Escenario 1: copiar template + criteria con remap CATALOGUE y THIRDS
    // ----------------------------------------------------------------

    @Test
    @DisplayName("copia AuxiliaryBook con criteria remapeado: accountId vía CATALOGUE, thirdId vía THIRDS")
    void copiar_auxBookConCriteria_remapeaCatalogueYThirds() {
        // Arrange
        UUID idProceso = UUID.randomUUID();

        CopyEquivalenciaDto eqCatalogue = CopyEquivalenciaDto.builder()
                .modulo("catalogue").tabla("cuentaContable").idViejo("100").idNuevo("1000").build();
        CopyEquivalenciaDto eqThird = CopyEquivalenciaDto.builder()
                .modulo("thirds").tabla("third").idViejo("200").idNuevo("2000").build();

        CopyPhaseRequestDto request = CopyPhaseRequestDto.builder()
                .idProceso(idProceso).fase(1)
                .entOrigen("ENT_A").entDestino("ENT_B")
                .snapshotCorte(Instant.now())
                .equivalenciasPrev(List.of(eqCatalogue, eqThird))
                .build();

        when(logRepo.buscarPorIdProcesoYFase(idProceso.toString(), 1))
                .thenReturn(Optional.empty());

        // Criteria con fromRange=100 (accountId) y thirdPartyId="200"
        AuxiliaryBookCriteriaEntity criteria = new AuxiliaryBookCriteriaEntity();
        criteria.setId(1L);
        criteria.setCriteriaType(ECriteriaType.ACCOUNT);
        criteria.setFromRange(100L);
        criteria.setToRange(100L);
        criteria.setThirdPartyId("200");
        criteria.setEndDate(LocalDate.now());

        AuxiliaryBookTemplateEntity template = new AuxiliaryBookTemplateEntity();
        template.setId(1L);
        template.setName("Template Test");
        template.setPathLogotype("/logo.png");
        template.setAligment(EAlignment.CENTER);
        template.setFont("Arial");
        template.setMainColor("#FFFFFF");

        AuxiliaryBookEntity book = new AuxiliaryBookEntity();
        book.setId(1L);
        book.setPublicId("pub-1");
        book.setType(EAuxiliaryBookType.ACCOUNT);
        book.setEntId("ENT_A");
        book.setUserId(1L);
        book.setFormat(EAuxiliaryBookFormat.PDF);
        book.setTemplate(template);
        book.setCriteria(criteria);

        when(sourceRepo.findAuxBooksForCopy(eq("ENT_A"), any())).thenReturn(List.of(book));
        when(sourceRepo.findScheduledReportsForCopy(eq("ENT_A"), any())).thenReturn(List.of());

        AuxiliaryBookEntity guardado = new AuxiliaryBookEntity();
        guardado.setId(10L);
        when(targetRepo.guardarAuxBook(any())).thenReturn(guardado);

        // Act
        CopyPhaseResponseDto response = service.ejecutar(request);

        // Assert
        assertThat(response.getEstado()).isEqualTo("COMPLETADO");
        // Verificar remap: fromRange de 100 → 1000
        verify(targetRepo).guardarAuxBook(argThat(b ->
                b.getCriteria() != null
                && b.getCriteria().getFromRange() != null
                && b.getCriteria().getFromRange().equals(1000L)
                && b.getCriteria().getToRange() != null
                && b.getCriteria().getToRange().equals(1000L)
                && "2000".equals(b.getCriteria().getThirdPartyId())));
    }

    // ----------------------------------------------------------------
    // Escenario 2: ScheduledReport se copia directamente
    // ----------------------------------------------------------------

    @Test
    @DisplayName("ScheduledReport se copia sin remap de FK externas")
    void copiar_scheduledReport_seCopiaSinRemap() {
        UUID idProceso = UUID.randomUUID();
        CopyPhaseRequestDto request = CopyPhaseRequestDto.builder()
                .idProceso(idProceso).fase(1)
                .entOrigen("ENT_A").entDestino("ENT_B")
                .snapshotCorte(Instant.now())
                .equivalenciasPrev(List.of())
                .build();

        when(logRepo.buscarPorIdProcesoYFase(idProceso.toString(), 1))
                .thenReturn(Optional.empty());

        when(sourceRepo.findAuxBooksForCopy(eq("ENT_A"), any())).thenReturn(List.of());

        ScheduledReportEntity report = new ScheduledReportEntity();
        report.setId(5L);
        report.setPublicId("sr-pub-5");
        report.setBookType(EAuxiliaryBookType.ACCOUNT);
        report.setFrequency(EFrequency.MONTHLY);
        report.setStartAt(Instant.now());
        report.setStatus(EJobStatus.ACTIVE);
        report.setEntId("ENT_A");
        report.setUserId(1L);

        when(sourceRepo.findScheduledReportsForCopy(eq("ENT_A"), any())).thenReturn(List.of(report));

        ScheduledReportEntity guardadoReport = new ScheduledReportEntity();
        guardadoReport.setId(50L);
        when(targetRepo.guardarScheduledReport(any())).thenReturn(guardadoReport);

        CopyPhaseResponseDto response = service.ejecutar(request);

        assertThat(response.getEstado()).isEqualTo("COMPLETADO");
        verify(targetRepo, times(1)).guardarScheduledReport(any());
    }

    // ----------------------------------------------------------------
    // Escenario 3: idempotencia
    // ----------------------------------------------------------------

    @Test
    @DisplayName("idempotencia: retorna resultado previo si ya existe log")
    void copiar_idempotencia_retornaResultadoPrevio() {
        UUID idProceso = UUID.randomUUID();
        CopyPhaseRequestDto request = CopyPhaseRequestDto.builder()
                .idProceso(idProceso).fase(1)
                .entOrigen("ENT_A").entDestino("ENT_B")
                .snapshotCorte(Instant.now()).build();

        CopyJobLog logPrevio = CopyJobLog.builder()
                .idProceso(idProceso).fase(1).modulo("auxbook")
                .estado(CopyEstado.COMPLETADO).equivalenciasGeneradas(2).build();

        when(logRepo.buscarPorIdProcesoYFase(idProceso.toString(), 1))
                .thenReturn(Optional.of(logPrevio));

        CopyPhaseResponseDto response = service.ejecutar(request);

        assertThat(response.getEstado()).isEqualTo("COMPLETADO");
        assertThat(response.getMensaje()).contains("idempotencia");
        verify(sourceRepo, never()).findAuxBooksForCopy(any(), any());
    }

    // ----------------------------------------------------------------
    // Escenario 4: origin == destination → ERROR_NO_REINTENTABLE
    // ----------------------------------------------------------------

    @Test
    @DisplayName("origen igual a destino retorna ERROR_NO_REINTENTABLE")
    void copiar_origenIgualDestino_retornaError() {
        CopyPhaseRequestDto request = CopyPhaseRequestDto.builder()
                .idProceso(UUID.randomUUID()).fase(1)
                .entOrigen("MISMA").entDestino("MISMA")
                .snapshotCorte(Instant.now()).build();

        CopyPhaseResponseDto response = service.ejecutar(request);

        assertThat(response.getEstado()).isEqualTo("ERROR_NO_REINTENTABLE");
        verify(sourceRepo, never()).findAuxBooksForCopy(any(), any());
    }
}
