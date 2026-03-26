package com.unicauca.edu.co.auxiliary_book.infrastructure.out.clients.email;

import com.unicauca.edu.co.auxiliary_book.application.ports.out.IEmailSenderPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService implements IEmailSenderPort {

    @Override
    public void sendReport(String to, String subject, String body, byte[] attachment, String fileName) {
        if (to == null || to.isBlank()) {
            throw new IllegalArgumentException("Email recipient is required.");
        }
        if (attachment == null || attachment.length == 0) {
            throw new IllegalArgumentException("Attachment content is empty.");
        }
        String resolvedSubject = subject == null || subject.isBlank() ? "Scheduled report" : subject;
        String resolvedBody = body == null ? "" : body;
        String resolvedFileName = fileName == null || fileName.isBlank() ? "scheduled_report.pdf" : fileName;

        log.info("Sending scheduled report email to {} with attachment {}. Subject: {}", to, resolvedFileName, resolvedSubject);
        // TODO: Integrate with real email provider.
        // This placeholder intentionally avoids external dependencies while keeping the port contract intact.
    }
}
