package com.unicauca.edu.co.auxiliary_book.infrastructure.out.clients.mail;

import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.unicauca.edu.co.auxiliary_book.domain.models.notification.EmailAttachment;
import com.unicauca.edu.co.auxiliary_book.domain.models.notification.EmailMessage;
import com.unicauca.edu.co.auxiliary_book.domain.ports.notification.IEmailNotificationPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Resend email adapter.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ResendMailService implements IEmailNotificationPort {

    private final WebClient.Builder externalWebClientBuilder;

    @Value("${resend.key}")
    private String apiKey;

    @Value("${resend.from:no-reply@auxiliary-book.local}")
    private String fromAddress;

    @Override
    public void sendEmail(EmailMessage emailMessage) {
        if (emailMessage == null) {
            throw new IllegalArgumentException("EmailMessage is required");
        }
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Resend API key is not configured");
        }
        try {
            ResendEmailRequest request = toRequest(emailMessage);
            externalWebClientBuilder.build()
                    .post()
                    .uri("https://api.resend.com/emails")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info("Email sent to {}", emailMessage.getTo());
        } catch (Exception ex) {
            log.error("Error sending email to {}", emailMessage.getTo(), ex);
            throw new IllegalStateException("Unable to send email", ex);
        }
    }

    private ResendEmailRequest toRequest(EmailMessage message) {
        List<ResendAttachment> attachments = message.getAttachments().stream()
                .map(this::toAttachment)
                .toList();
        return new ResendEmailRequest(
                fromAddress,
                message.getTo(),
                message.getSubject(),
                message.getBody(),
                attachments
        );
    }

    private ResendAttachment toAttachment(EmailAttachment attachment) {
        return new ResendAttachment(
                attachment.getFileName(),
                attachment.getContentType(),
                Base64.getEncoder().encodeToString(attachment.getContent())
        );
    }

    private record ResendEmailRequest(String from, String to, String subject, String html,
                                      List<ResendAttachment> attachments) {
    }

    private record ResendAttachment(String filename, String contentType, String content) {
    }
}
