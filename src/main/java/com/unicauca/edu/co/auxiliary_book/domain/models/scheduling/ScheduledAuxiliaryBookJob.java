package com.unicauca.edu.co.auxiliary_book.domain.models.scheduling;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.AuxiliaryBookCriteria;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookType;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EJobStatus;
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
    private DeliveryConfig deliveryConfig;
    private LocalDateTime createdAt;
    private LocalDateTime endAt;
    private String createdBy; // Public ID user
    private String entId;
    private Long userId;
    private EJobStatus status;
}
