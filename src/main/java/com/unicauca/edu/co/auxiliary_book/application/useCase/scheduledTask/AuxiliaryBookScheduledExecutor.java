package com.unicauca.edu.co.auxiliary_book.application.useCase.scheduledTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicauca.edu.co.auxiliary_book.application.ports.in.auxiliaryBook.IAuxiliaryBookCommandPort;
import com.unicauca.edu.co.auxiliary_book.application.ports.in.export.IExportReportPort;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.export.AuxiliaryBookTemplate;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.export.ExportInfo;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAlignment;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookFormat;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EDeliveryWay;
import com.unicauca.edu.co.auxiliary_book.domain.models.notification.EmailAttachment;
import com.unicauca.edu.co.auxiliary_book.domain.models.notification.EmailMessage;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTask;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTaskType;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.TaskExecutionResult;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.payload.AuxiliaryBookScheduledPayload;
import com.unicauca.edu.co.auxiliary_book.domain.ports.notification.IEmailNotificationPort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.storage.IFileStoragePort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuxiliaryBookScheduledExecutor {

    private final ObjectMapper objectMapper;
    private final IAuxiliaryBookCommandPort auxiliaryBookCommandPort;
    private final IExportReportPort exportReportPort;
    private final IFileStoragePort fileStoragePort;
    private final IEmailNotificationPort emailNotificationPort;

    public TaskExecutionResult execute(ScheduledTask task, ScheduledTaskType expectedType) {
        try {
            AuxiliaryBookScheduledPayload payload = mapPayload(task);
            if (!Objects.equals(task.getTaskType(), expectedType)) {
                return TaskExecutionResult.failure("Unexpected task type");
            }

            if (payload.getFormat() == null) {
                return TaskExecutionResult.failure("Report format is required");
            }

            if (payload.getDeliveryWay() == EDeliveryWay.EMAIL && (payload.getEmailTo() == null
                    || payload.getEmailTo().isBlank())) {
                return TaskExecutionResult.failure("Email destination is required");
            }

            AuxiliaryBook auxiliaryBook = payload.getAuxiliaryBook();
            if (auxiliaryBook == null) {
                return TaskExecutionResult.failure("Auxiliary book information is required");
            }
            if (payload.getEntName() == null || payload.getEntName().isBlank()) {
                return TaskExecutionResult.failure("Enterprise name is required");
            }
            auxiliaryBook.changeFormat(payload.getFormat());

            AuxiliaryBook registeredBook = this.auxiliaryBookCommandPort.registerAuxiliaryBook(auxiliaryBook);
            List<?> data = this.auxiliaryBookCommandPort.genereteAuxiliaryBookInfo(registeredBook);
            if (data == null) {
                data = List.of();
            }

            AuxiliaryBookTemplate template = ensureTemplate(payload.getTemplate());
            ExportInfo exportInfo = new ExportInfo(payload.getFormat(), payload.getEntName(), registeredBook, data,
                    template);

            byte[] reportBytes = this.exportReportPort.exportReport(exportInfo);
            if (reportBytes == null || reportBytes.length == 0) {
                return TaskExecutionResult.failure("Report generation returned empty content");
            }

            String fileName = buildFileName(registeredBook, payload.getFormat());
            String artifactPath = this.fileStoragePort.store(reportBytes, fileName);

            if (payload.getDeliveryWay() == EDeliveryWay.EMAIL) {
                sendEmail(payload, reportBytes, fileName, registeredBook);
            }

            return TaskExecutionResult.success(artifactPath);
        } catch (Exception ex) {
            log.error("Error executing auxiliary book scheduled task {}", task.getPublicId(), ex);
            return TaskExecutionResult.failure(ex.getMessage());
        }
    }

    private AuxiliaryBookScheduledPayload mapPayload(ScheduledTask task) {
        return this.objectMapper.convertValue(task.getPayload(), AuxiliaryBookScheduledPayload.class);
    }

    private AuxiliaryBookTemplate ensureTemplate(AuxiliaryBookTemplate template) {
        if (template != null) {
            return template;
        }
        try {
            return AuxiliaryBookTemplate.builder()
                    .name("Auxiliary Book")
                    .font("Arial")
                    .fontSize(12)
                    .mainColor("#000000")
                    .alienation(EAlignment.LEFT)
                    .pathLogotype(new URL("https://example.com/logo.png"))
                    .build();
        } catch (MalformedURLException e) {
            return AuxiliaryBookTemplate.builder()
                    .name("Auxiliary Book")
                    .font("Arial")
                    .fontSize(12)
                    .mainColor("#000000")
                    .alienation(EAlignment.LEFT)
                    .build();
        }
    }

    private void sendEmail(AuxiliaryBookScheduledPayload payload, byte[] reportBytes, String fileName,
                           AuxiliaryBook book) {
        EmailAttachment attachment = EmailAttachment.builder()
                .fileName(fileName)
                .contentType(resolveContentType(payload.getFormat()))
                .content(reportBytes)
                .build();

        String subject = payload.getEmailSubject() != null ? payload.getEmailSubject()
                : "Libro auxiliar generado";
        String body = payload.getEmailBody() != null ? payload.getEmailBody()
                : "El libro auxiliar " + book.getPublicId() + " ha sido generado.";

        EmailMessage emailMessage = EmailMessage.builder()
                .to(payload.getEmailTo())
                .subject(subject)
                .body(body)
                .attachments(List.of(attachment))
                .build();
        this.emailNotificationPort.sendEmail(emailMessage);
    }

    private String buildFileName(AuxiliaryBook book, EAuxiliaryBookFormat format) {
        String extension = format == EAuxiliaryBookFormat.EXCEL ? "xls" : "pdf";
        return "auxiliary_book_" + book.getPublicId() + "_" + Instant.now().toEpochMilli() + "." + extension;
    }

    private String resolveContentType(EAuxiliaryBookFormat format) {
        return switch (format) {
            case PDF -> "application/pdf";
            case EXCEL -> "application/vnd.ms-excel";
        };
    }
}
