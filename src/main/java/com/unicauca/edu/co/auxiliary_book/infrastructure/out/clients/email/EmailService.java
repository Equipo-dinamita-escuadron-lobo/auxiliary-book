package com.unicauca.edu.co.auxiliary_book.infrastructure.out.clients.email;

import com.unicauca.edu.co.auxiliary_book.application.ports.out.IEmailSenderPort;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.Attachment;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

/**
 * @brief Servicio de envío de correos electrónicos usando Resend.
 *
 * Implementación del puerto {@link IEmailSenderPort} que envía reportes
 * programados con adjuntos codificados en Base64 a través del API de
 * Resend. Valida destinatarios, contenido del adjunto y la presencia
 * de las credenciales/remitente configurados.
 */
@Service
@Slf4j
public class EmailService implements IEmailSenderPort {

    private static final String DEFAULT_SUBJECT = "Scheduled report";
    private static final String DEFAULT_BODY = "Your scheduled report is attached.";
    private static final String DEFAULT_FILE_NAME = "scheduled_report.pdf";

    private final String resendApiKey;
    private final String resendFrom;

    public EmailService(
            @Value("${resend.key:}") String resendApiKey,
            @Value("${resend.from:}") String resendFrom
    ) {
        this.resendApiKey = resendApiKey;
        this.resendFrom = resendFrom;
    }

    @Override
    public void sendReport(String to, String subject, String body, byte[] attachment, String fileName) {
        List<String> recipients = resolveRecipients(to);
        validateAttachment(attachment);
        validateConfiguration();

        String resolvedSubject = subject == null || subject.isBlank() ? DEFAULT_SUBJECT : subject;
        String resolvedBody = body == null || body.isBlank() ? DEFAULT_BODY : body;
        String resolvedFileName = fileName == null || fileName.isBlank() ? DEFAULT_FILE_NAME : fileName;

        Attachment resendAttachment = Attachment.builder()
                .fileName(resolvedFileName)
                .content(Base64.getEncoder().encodeToString(attachment))
                .build();

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(resendFrom)
                .to(recipients)
                .subject(resolvedSubject)
                .text(resolvedBody)
                .attachments(List.of(resendAttachment))
                .build();

        try {
            CreateEmailResponse response = new Resend(resendApiKey).emails().send(params);
            log.info(
                    "Scheduled report email sent via Resend to {} with attachment {}. Subject: {}. EmailId: {}",
                    recipients,
                    resolvedFileName,
                    resolvedSubject,
                    response != null ? response.getId() : null
            );
        } catch (ResendException ex) {
            throw new IllegalStateException("Resend email delivery failed: " + ex.getMessage(), ex);
        }
    }

    private List<String> resolveRecipients(String to) {
        if (to == null || to.isBlank()) {
            throw new IllegalArgumentException("Email recipient is required.");
        }

        List<String> recipients = List.of(to.split("[,;]"))
                .stream()
                .map(String::trim)
                .filter(recipient -> !recipient.isBlank())
                .toList();

        if (recipients.isEmpty()) {
            throw new IllegalArgumentException("Email recipient is required.");
        }

        return recipients;
    }

    private void validateAttachment(byte[] attachment) {
        if (attachment == null || attachment.length == 0) {
            throw new IllegalArgumentException("Attachment content is empty.");
        }
    }

    private void validateConfiguration() {
        if (resendApiKey == null || resendApiKey.isBlank()) {
            throw new IllegalStateException("Resend API key is not configured.");
        }
        if (resendFrom == null || resendFrom.isBlank()) {
            throw new IllegalStateException("Resend sender address is not configured.");
        }
    }
}
