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
public class ScheduledReportExecutionResponse {
    private UUID executionId;
    private UUID jobId;
    private Instant scheduledAt;
    private Instant startedAt;
    private Instant finishedAt;
    private EExecutionStatus statusExecution;
    private EDeliveryStatus deliveryStatus;
    private String errorMessage;
}
