package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.response;

import java.time.Instant;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTaskStatus;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTaskType;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduledTaskResponseDTO {
    String publicId;
    ScheduledTaskType taskType;
    ScheduledTaskStatus status;
    Instant executeAt;
    Map<String, Object> payload;
    String artifactPath;
    Instant lastExecutionAt;
    String lastError;
    Integer attempts;
}
