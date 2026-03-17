package com.unicauca.edu.co.auxiliary_book.domain.models.scheduling;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.AuxiliaryBookCriteria;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduledAuxiliaryBookJob {
    private Long jobId;
    private String publicId;
    private EAuxiliaryBookType bookType;
    private AuxiliaryBookCriteria criteria;
    private ScheduleSpec scheduleSpec;
    private LocalDateTime createdAt;
    private LocalDateTime endAt;
    private String createdBy; // Public ID user
}
