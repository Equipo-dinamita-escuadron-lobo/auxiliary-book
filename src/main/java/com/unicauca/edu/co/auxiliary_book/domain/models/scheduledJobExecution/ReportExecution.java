package com.unicauca.edu.co.auxiliary_book.domain.models.scheduledJobExecution;

import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookFormat;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EDeliveryStatus;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EExecutionStatus;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportExecution {
    private UUID executionId;
    private UUID jobId;
    private Instant scheduledAt;
    private Instant startedAt;
    private Instant finishedAt;
    private EExecutionStatus statusExecution;
    private EDeliveryStatus deliveryStatus;
    private Map<EAuxiliaryBookFormat,String> artifactRefs;
    private String errorCode;
    private String errorMessage;
    private String correlationId;
    private int retryCount;
}
