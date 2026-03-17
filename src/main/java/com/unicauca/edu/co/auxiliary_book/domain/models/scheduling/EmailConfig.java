package com.unicauca.edu.co.auxiliary_book.domain.models.scheduling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailConfig {
    private String to;
    private String subjectTemplate;
    private String bodyTemplate;
}
