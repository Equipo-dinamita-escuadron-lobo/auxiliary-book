package com.unicauca.edu.co.auxiliary_book.domain.models.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Value object representing an email attachment.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailAttachment {

    private String fileName;
    private String contentType;
    private byte[] content;
}
