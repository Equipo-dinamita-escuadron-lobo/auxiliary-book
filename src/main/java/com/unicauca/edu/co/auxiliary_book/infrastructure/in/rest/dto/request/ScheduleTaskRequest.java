package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.request;

import java.time.ZonedDateTime;
import java.util.Map;

import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTaskType;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ScheduleTaskRequest(
        @NotNull ScheduledTaskType taskType,
        @NotNull @Future ZonedDateTime executeAt,
        @NotEmpty Map<String, Object> payload) {
}
