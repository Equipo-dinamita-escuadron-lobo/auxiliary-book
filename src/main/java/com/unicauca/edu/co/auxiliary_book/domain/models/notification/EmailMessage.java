package com.unicauca.edu.co.auxiliary_book.domain.models.notification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Value object representing an outbound email.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessage {

    private String to;
    private String subject;
    private String body;
    @Builder.Default
    private List<EmailAttachment> attachments = new ArrayList<>();

    public List<EmailAttachment> getAttachments() {
        return Collections.unmodifiableList(attachments);
    }

    public void setAttachments(List<EmailAttachment> attachments) {
        if (attachments == null) {
            this.attachments = new ArrayList<>();
            return;
        }
        this.attachments = new ArrayList<>(attachments);
    }
}
