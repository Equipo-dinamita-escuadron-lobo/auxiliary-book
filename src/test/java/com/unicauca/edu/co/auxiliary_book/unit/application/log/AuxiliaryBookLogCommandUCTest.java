package com.unicauca.edu.co.auxiliary_book.unit.application.log;

import com.unicauca.edu.co.auxiliary_book.application.ports.in.auxiliaryBook.IAuxiliaryBookCommandPort;
import com.unicauca.edu.co.auxiliary_book.application.ports.in.export.IExportReportPort;
import com.unicauca.edu.co.auxiliary_book.application.ports.out.IAccountingInfoClient;
import com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.AuxiliaryBookCommandUC;
import com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils.AuxiliaryBookProcessor;
import com.unicauca.edu.co.auxiliary_book.application.useCase.log.AuxiliaryBookLogCommandUC;
import com.unicauca.edu.co.auxiliary_book.application.useCase.scheduledReport.jobSteps.GenerateReportStep;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.commands.JobCommandContext;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.AuxiliaryBookCriteria;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.export.ExportInfo;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookFormat;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookType;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.ECriteriaType;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EState;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.ETypeEvent;
import com.unicauca.edu.co.auxiliary_book.domain.models.history.AuxiliaryBookHistory;
import com.unicauca.edu.co.auxiliary_book.domain.models.log.AuxiliaryBookLog;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduling.ScheduleSpec;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduling.ScheduledAuxiliaryBookJob;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBook.IAuxiliaryBookCommandRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookHistory.IAuxiliaryBookHistoryCommandRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookLog.IAuxiliaryBookLogCommandRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.IFormatterResultOutputPort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.IMessageServicePort;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.exception.customized.GenericErrorException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @brief Pruebas unitarias para {@link AuxiliaryBookLogCommandUC},
 * {@link AuxiliaryBookCommandUC} y {@link GenerateReportStep}.
 */
@ExtendWith(MockitoExtension.class)
class AuxiliaryBookLogCommandUCTest {

    @Mock
    private IAuxiliaryBookLogCommandRepositoryPort repositoryPort;

    @Mock
    private IAuxiliaryBookCommandRepositoryPort auxiliaryBookCommandRepositoryPort;

    @Mock
    private IAuxiliaryBookHistoryCommandRepositoryPort auxiliaryBookHistoryCommandRepositoryPort;

    @Mock
    private IAccountingInfoClient accountingInfoClient;

    @Mock
    private AuxiliaryBookProcessor auxiliaryBookProcessor;

    @Mock
    private IFormatterResultOutputPort formatterResultOutputPort;

    @Mock
    private IMessageServicePort messageServicePort;

    @Mock
    private IAuxiliaryBookCommandPort auxiliaryBookCommandPort;

    @Mock
    private IExportReportPort exportReportPort;

    @InjectMocks
    private AuxiliaryBookLogCommandUC useCase;

    @Test
    @DisplayName("registerAuxiliaryBookLog delega en el repositorio y retorna su resultado")
    void registerAuxiliaryBookLogDelegates() {
        AuxiliaryBookLog input = AuxiliaryBookLog.builder().message("evento").build();
        AuxiliaryBookLog persisted = AuxiliaryBookLog.builder().id(10L).message("evento").build();

        Mockito.when(repositoryPort.registerAuxiliaryBookLog(input)).thenReturn(persisted);

        AuxiliaryBookLog result = useCase.registerAuxiliaryBookLog(input);

        Assertions.assertThat(result).isSameAs(persisted);
        Mockito.verify(repositoryPort).registerAuxiliaryBookLog(input);
        Mockito.verifyNoMoreInteractions(repositoryPort);
    }

    @Test
    @DisplayName("registerAuxiliaryBook debe generar publicId, historial GENERATED y log REGISTERED")
    void registerAuxiliaryBookCreatesHistoryAndLog() {
        AuxiliaryBookCommandUC commandUC = new AuxiliaryBookCommandUC(
                auxiliaryBookCommandRepositoryPort,
                repositoryPort,
                auxiliaryBookHistoryCommandRepositoryPort,
                accountingInfoClient,
                auxiliaryBookProcessor,
                formatterResultOutputPort,
                messageServicePort
        );
        AuxiliaryBook input = AuxiliaryBook.builder()
                .type(EAuxiliaryBookType.ACCOUNT)
                .entId("ENT1")
                .userId(30L)
                .criteria(criteria())
                .format(EAuxiliaryBookFormat.PDF)
                .build();

        Mockito.when(auxiliaryBookCommandRepositoryPort.registerAuxiliaryBook(Mockito.any(AuxiliaryBook.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AuxiliaryBook result = commandUC.registerAuxiliaryBook(input);

        ArgumentCaptor<AuxiliaryBookHistory> historyCaptor = ArgumentCaptor.forClass(AuxiliaryBookHistory.class);
        ArgumentCaptor<AuxiliaryBookLog> logCaptor = ArgumentCaptor.forClass(AuxiliaryBookLog.class);

        Mockito.verify(auxiliaryBookHistoryCommandRepositoryPort).registerAuxiliaryBookHistory(historyCaptor.capture());
        Mockito.verify(repositoryPort).registerAuxiliaryBookLog(logCaptor.capture());

        Assertions.assertThat(result.getPublicId()).isNotBlank();
        Assertions.assertThat(historyCaptor.getValue().getState()).isEqualTo(EState.GENERATED);
        Assertions.assertThat(historyCaptor.getValue().getAuxiliaryBook()).isSameAs(result);
        Assertions.assertThat(logCaptor.getValue().getETypeEvent()).isEqualTo(ETypeEvent.REGISTERED);
        Assertions.assertThat(logCaptor.getValue().getMessage()).contains("registered successfully");
    }

    @Test
    @DisplayName("registerAuxiliaryBook debe delegar al formatter cuando el libro es nulo")
    void registerAuxiliaryBookDelegatesToFormatterForNullBook() {
        AuxiliaryBookCommandUC commandUC = new AuxiliaryBookCommandUC(
                auxiliaryBookCommandRepositoryPort,
                repositoryPort,
                auxiliaryBookHistoryCommandRepositoryPort,
                accountingInfoClient,
                auxiliaryBookProcessor,
                formatterResultOutputPort,
                messageServicePort
        );

        Mockito.when(messageServicePort.getMessage(Mockito.anyString(), Mockito.anyString())).thenReturn("AuxiliaryBook");
        Mockito.doThrow(new GenericErrorException(400, "AuxiliaryBook"))
                .when(formatterResultOutputPort)
                .returnErrorGenericResponse(Mockito.eq(400), Mockito.anyString());

        Assertions.assertThatThrownBy(() -> commandUC.registerAuxiliaryBook(null))
                .isInstanceOf(GenericErrorException.class)
                .hasMessageContaining("AuxiliaryBook");

        Mockito.verifyNoInteractions(auxiliaryBookCommandRepositoryPort, auxiliaryBookHistoryCommandRepositoryPort, repositoryPort);
    }

    @Test
    @DisplayName("genereteAuxiliaryBookInfo debe registrar logs de inicio y éxito")
    void generateAuxiliaryBookInfoLogsStartAndSuccess() {
        AuxiliaryBookCommandUC commandUC = new AuxiliaryBookCommandUC(
                auxiliaryBookCommandRepositoryPort,
                repositoryPort,
                auxiliaryBookHistoryCommandRepositoryPort,
                accountingInfoClient,
                auxiliaryBookProcessor,
                formatterResultOutputPort,
                messageServicePort
        );
        AuxiliaryBook book = AuxiliaryBook.builder()
                .publicId("book-public-id")
                .criteria(criteria())
                .build();
        List<String> processedData = List.of("a", "b");

        Mockito.doReturn(processedData)
                .when(auxiliaryBookProcessor)
                .processAuxiliaryBookData(accountingInfoClient, book);

        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) commandUC.genereteAuxiliaryBookInfo(book);

        ArgumentCaptor<AuxiliaryBookLog> logCaptor = ArgumentCaptor.forClass(AuxiliaryBookLog.class);
        Mockito.verify(repositoryPort, Mockito.times(2)).registerAuxiliaryBookLog(logCaptor.capture());

        List<AuxiliaryBookLog> logs = logCaptor.getAllValues();
        Assertions.assertThat(result).containsExactly("a", "b");
        Assertions.assertThat(logs.get(0).getETypeEvent()).isEqualTo(ETypeEvent.GENERATING);
        Assertions.assertThat(logs.get(1).getETypeEvent()).isEqualTo(ETypeEvent.SUCCESS_GENERATION);
        Assertions.assertThat(logs.get(1).getMessage()).contains("2 items processed");
    }

    @Test
    @DisplayName("genereteAuxiliaryBookInfo debe registrar error y delegar al formatter cuando falla el procesamiento")
    void generateAuxiliaryBookInfoLogsErrorAndDelegatesToFormatter() {
        AuxiliaryBookCommandUC commandUC = new AuxiliaryBookCommandUC(
                auxiliaryBookCommandRepositoryPort,
                repositoryPort,
                auxiliaryBookHistoryCommandRepositoryPort,
                accountingInfoClient,
                auxiliaryBookProcessor,
                formatterResultOutputPort,
                messageServicePort
        );
        AuxiliaryBook book = AuxiliaryBook.builder()
                .publicId("book-public-id")
                .criteria(criteria())
                .build();

        Mockito.when(auxiliaryBookProcessor.processAuxiliaryBookData(accountingInfoClient, book))
                .thenThrow(new IllegalStateException("boom"));
        Mockito.when(messageServicePort.getMessage(Mockito.anyString(), Mockito.anyString())).thenReturn("formatted");
        Mockito.doThrow(new GenericErrorException(500, "formatted"))
                .when(formatterResultOutputPort)
                .returnErrorGenericResponse(Mockito.eq(500), Mockito.anyString());

        Assertions.assertThatThrownBy(() -> commandUC.genereteAuxiliaryBookInfo(book))
                .isInstanceOf(GenericErrorException.class)
                .hasMessageContaining("formatted");

        ArgumentCaptor<AuxiliaryBookLog> logCaptor = ArgumentCaptor.forClass(AuxiliaryBookLog.class);
        Mockito.verify(repositoryPort, Mockito.times(2)).registerAuxiliaryBookLog(logCaptor.capture());
        Assertions.assertThat(logCaptor.getAllValues().get(1).getETypeEvent()).isEqualTo(ETypeEvent.ERROR_GENERATION);
        Assertions.assertThat(logCaptor.getAllValues().get(1).getMessage()).contains("boom");
    }

    @Test
    @DisplayName("GenerateReportStep debe construir el libro, exportar y dejar atributos en el contexto")
    void generateReportStepStoresArtifactsInContext() {
        GenerateReportStep step = new GenerateReportStep(auxiliaryBookCommandPort, exportReportPort);
        ScheduledAuxiliaryBookJob job = scheduledJob(EAuxiliaryBookType.ACCOUNT);
        JobCommandContext context = new JobCommandContext(
                UUID.randomUUID(),
                UUID.randomUUID(),
                Instant.parse("2025-01-20T08:00:00Z"),
                "corr-1",
                "scheduler",
                Map.of(JobCommandContext.ATTRIBUTE_JOB, job)
        );
        AuxiliaryBook registeredBook = AuxiliaryBook.builder()
                .publicId("book-public-id")
                .type(EAuxiliaryBookType.ACCOUNT)
                .entId("ENT1")
                .userId(90L)
                .criteria(criteria())
                .format(EAuxiliaryBookFormat.PDF)
                .build();
        List<String> reportData = List.of("fila-1", "fila-2");

        Mockito.when(auxiliaryBookCommandPort.registerAuxiliaryBook(Mockito.any(AuxiliaryBook.class))).thenReturn(registeredBook);
        Mockito.doReturn(reportData).when(auxiliaryBookCommandPort).genereteAuxiliaryBookInfo(registeredBook);
        Mockito.when(exportReportPort.exportReport(Mockito.any(ExportInfo.class))).thenReturn(new byte[]{1, 2, 3});

        step.execute(context);

        ArgumentCaptor<ExportInfo> exportInfoCaptor = ArgumentCaptor.forClass(ExportInfo.class);
        Mockito.verify(exportReportPort).exportReport(exportInfoCaptor.capture());

        ExportInfo exportInfo = exportInfoCaptor.getValue();
        Assertions.assertThat(exportInfo.getFormat()).isEqualTo(EAuxiliaryBookFormat.PDF);
        Assertions.assertThat(exportInfo.getEntName()).isEqualTo("ENT1");
        Assertions.assertThat(exportInfo.getAuxiliaryBook()).isSameAs(registeredBook);
        Assertions.assertThat(exportInfo.getInfoReportTemplate().getName()).isEqualTo("ACCOUNT");
        Assertions.assertThat(context.getRequiredAttribute(JobCommandContext.ATTRIBUTE_REGISTERED_BOOK, AuxiliaryBook.class))
                .isSameAs(registeredBook);
        @SuppressWarnings("unchecked")
        List<String> reportDataResult = (List<String>) context.getRequiredAttribute(JobCommandContext.ATTRIBUTE_REPORT_DATA, List.class);
        Assertions.assertThat(reportDataResult)
                .isEqualTo(reportData);
        Assertions.assertThat(context.getRequiredAttribute(JobCommandContext.ATTRIBUTE_REPORT_BYTES, byte[].class))
                .containsExactly(1, 2, 3);
    }

    @Test
    @DisplayName("GenerateReportStep debe fallar cuando la exportación produce contenido vacío")
    void generateReportStepFailsOnEmptyReportBytes() {
        GenerateReportStep step = new GenerateReportStep(auxiliaryBookCommandPort, exportReportPort);
        ScheduledAuxiliaryBookJob job = scheduledJob(EAuxiliaryBookType.THIRD_PARTY);
        JobCommandContext context = new JobCommandContext(
                UUID.randomUUID(),
                UUID.randomUUID(),
                Instant.parse("2025-01-20T08:00:00Z"),
                "corr-2",
                "scheduler",
                Map.of(
                        JobCommandContext.ATTRIBUTE_JOB, job,
                        JobCommandContext.ATTRIBUTE_REPORT_FORMAT, EAuxiliaryBookFormat.EXCEL
                )
        );
        AuxiliaryBook registeredBook = AuxiliaryBook.builder()
                .publicId("book-public-id")
                .type(EAuxiliaryBookType.THIRD_PARTY)
                .entId("ENT1")
                .userId(90L)
                .criteria(criteria())
                .format(EAuxiliaryBookFormat.EXCEL)
                .build();
        List<String> reportData = List.of("fila");

        Mockito.when(auxiliaryBookCommandPort.registerAuxiliaryBook(Mockito.any(AuxiliaryBook.class))).thenReturn(registeredBook);
        Mockito.doReturn(reportData).when(auxiliaryBookCommandPort).genereteAuxiliaryBookInfo(registeredBook);
        Mockito.when(exportReportPort.exportReport(Mockito.any(ExportInfo.class))).thenReturn(new byte[0]);

        Assertions.assertThatThrownBy(() -> step.execute(context))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("empty");
    }

    private AuxiliaryBookCriteria criteria() {
        return AuxiliaryBookCriteria.builder()
                .criteriaType(ECriteriaType.ACCOUNT)
                .startDate(LocalDate.of(2025, 1, 1))
                .endDate(LocalDate.of(2025, 1, 31))
                .build();
    }

    private ScheduledAuxiliaryBookJob scheduledJob(EAuxiliaryBookType bookType) {
        ScheduledAuxiliaryBookJob job = new ScheduledAuxiliaryBookJob();
        job.setBookType(bookType);
        job.setEntId("ENT1");
        job.setUserId(90L);
        job.setCriteria(criteria());
        job.setScheduleSpec(new ScheduleSpec(
                com.unicauca.edu.co.auxiliary_book.domain.models.enums.EFrequency.DAILY,
                Instant.parse("2025-01-20T08:00:00Z"),
                Optional.empty(),
                Instant.parse("2025-01-20T08:00:00Z")
        ));
        return job;
    }
}
