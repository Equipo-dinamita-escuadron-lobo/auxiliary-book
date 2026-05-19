package com.unicauca.edu.co.auxiliary_book.copy.application;

import com.unicauca.edu.co.auxiliary_book.copy.application.output.IAuxBookCopyJobLogRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.copy.application.output.IAuxBookSourceRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.copy.application.output.IAuxBookTargetRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.copy.application.services.AuxBookCopyService;
import com.unicauca.edu.co.auxiliary_book.copy.infrastructure.in.rest.dto.CopyEquivalenciaDto;
import com.unicauca.edu.co.auxiliary_book.copy.infrastructure.in.rest.dto.CopyPhaseRequestDto;
import com.unicauca.edu.co.auxiliary_book.copy.infrastructure.in.rest.dto.CopyPhaseResponseDto;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAlignment;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookType;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.ECriteriaType;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookCriteriaEntity;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookEntity;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookTemplateEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
 * Tests de remap de FK para auxiliary-book copy.
 * Verifica: accountId vía CATALOGUE, thirdId vía THIRDS.
 * RED phase: Fase 9.7.
 */
@ExtendWith(MockitoExtension.class)
class AuxBookCopyFkRemapTest {

    @Mock private IAuxBookCopyJobLogRepositoryPort logRepo;
    @Mock private IAuxBookSourceRepositoryPort sourceRepo;
    @Mock private IAuxBookTargetRepositoryPort targetRepo;

    private AuxBookCopyService service;

    @BeforeEach
    void setUp() {
        service = new AuxBookCopyService(logRepo, sourceRepo, targetRepo);
    }

    @Test
    @DisplayName("criteria.fromRange remapeado vía tabla 'cuentaContable' (CATALOGUE)")
    void remap_fromRange_viaCatalogue() {
        UUID idProceso = UUID.randomUUID();

        CopyEquivalenciaDto eqCatalogue = CopyEquivalenciaDto.builder()
                .modulo("catalogue").tabla("cuentaContable").idViejo("500").idNuevo("5000").build();

        CopyPhaseRequestDto request = CopyPhaseRequestDto.builder()
                .idProceso(idProceso).fase(1)
                .entOrigen("ENT_A").entDestino("ENT_B")
                .snapshotCorte(Instant.now())
                .equivalenciasPrev(List.of(eqCatalogue))
                .build();

        when(logRepo.buscarPorIdProcesoYFase(idProceso.toString(), 1)).thenReturn(Optional.empty());

        AuxiliaryBookCriteriaEntity criteria = buildCriteria(500L, null);
        AuxiliaryBookEntity book = buildBook(criteria);

        when(sourceRepo.findAuxBooksForCopy(eq("ENT_A"), any())).thenReturn(List.of(book));
        when(sourceRepo.findScheduledReportsForCopy(eq("ENT_A"), any())).thenReturn(List.of());

        AuxiliaryBookEntity guardado = new AuxiliaryBookEntity();
        guardado.setId(99L);
        ArgumentCaptor<AuxiliaryBookEntity> captor = ArgumentCaptor.forClass(AuxiliaryBookEntity.class);
        when(targetRepo.guardarAuxBook(captor.capture())).thenReturn(guardado);

        CopyPhaseResponseDto response = service.ejecutar(request);

        assertThat(response.getEstado()).isEqualTo("COMPLETADO");
        assertThat(captor.getValue().getCriteria().getFromRange()).isEqualTo(5000L);
    }

    @Test
    @DisplayName("criteria.thirdPartyId remapeado vía tabla 'third' (THIRDS)")
    void remap_thirdPartyId_viaThirds() {
        UUID idProceso = UUID.randomUUID();

        CopyEquivalenciaDto eqThird = CopyEquivalenciaDto.builder()
                .modulo("thirds").tabla("third").idViejo("TH-OLD").idNuevo("TH-NEW").build();

        CopyPhaseRequestDto request = CopyPhaseRequestDto.builder()
                .idProceso(idProceso).fase(1)
                .entOrigen("ENT_A").entDestino("ENT_B")
                .snapshotCorte(Instant.now())
                .equivalenciasPrev(List.of(eqThird))
                .build();

        when(logRepo.buscarPorIdProcesoYFase(idProceso.toString(), 1)).thenReturn(Optional.empty());

        AuxiliaryBookCriteriaEntity criteria = buildCriteria(null, "TH-OLD");
        AuxiliaryBookEntity book = buildBook(criteria);

        when(sourceRepo.findAuxBooksForCopy(eq("ENT_A"), any())).thenReturn(List.of(book));
        when(sourceRepo.findScheduledReportsForCopy(eq("ENT_A"), any())).thenReturn(List.of());

        AuxiliaryBookEntity guardado = new AuxiliaryBookEntity();
        guardado.setId(99L);
        ArgumentCaptor<AuxiliaryBookEntity> captor = ArgumentCaptor.forClass(AuxiliaryBookEntity.class);
        when(targetRepo.guardarAuxBook(captor.capture())).thenReturn(guardado);

        CopyPhaseResponseDto response = service.ejecutar(request);

        assertThat(response.getEstado()).isEqualTo("COMPLETADO");
        assertThat(captor.getValue().getCriteria().getThirdPartyId()).isEqualTo("TH-NEW");
    }

    // ----------------------------------------------------------------
    // Helpers
    // ----------------------------------------------------------------

    private AuxiliaryBookCriteriaEntity buildCriteria(Long fromRange, String thirdPartyId) {
        AuxiliaryBookCriteriaEntity c = new AuxiliaryBookCriteriaEntity();
        c.setId(1L);
        c.setCriteriaType(ECriteriaType.ACCOUNT);
        c.setFromRange(fromRange);
        c.setToRange(fromRange);
        c.setThirdPartyId(thirdPartyId);
        c.setEndDate(LocalDate.now());
        return c;
    }

    private AuxiliaryBookEntity buildBook(AuxiliaryBookCriteriaEntity criteria) {
        AuxiliaryBookTemplateEntity tpl = new AuxiliaryBookTemplateEntity();
        tpl.setId(1L);
        tpl.setName("TPL");
        tpl.setPathLogotype("/logo.png");
        tpl.setAligment(EAlignment.LEFT);
        tpl.setFont("Arial");
        tpl.setMainColor("#000000");

        AuxiliaryBookEntity book = new AuxiliaryBookEntity();
        book.setId(1L);
        book.setPublicId("pub-1");
        book.setType(EAuxiliaryBookType.ACCOUNT);
        book.setEntId("ENT_A");
        book.setUserId(1L);
        book.setTemplate(tpl);
        book.setCriteria(criteria);
        return book;
    }
}
