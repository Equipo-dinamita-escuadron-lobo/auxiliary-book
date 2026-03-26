package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.response.scheduledReport;

import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EDeliveryStatus;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EExecutionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduledReportExecutionListItemResponse {
    private UUID executionId;
    private Instant scheduledAt;
    private EExecutionStatus statusExecution;
    private EDeliveryStatus deliveryStatus;
    private String errorMessage;
}
