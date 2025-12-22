package com.unicauca.edu.co.auxiliary_book.application.useCase.scheduledTask;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.unicauca.edu.co.auxiliary_book.application.ports.in.scheduledTasks.IScheduledTaskCommandPort;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTask;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.TaskExecutionTime;
import com.unicauca.edu.co.auxiliary_book.domain.ports.IFormatterResultOutputPort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.IMessageServicePort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.ScheduledTasks.IScheduledTaskCommandRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.infrastructure.config.i18n.MessageKeys;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledTaskCommandUC implements IScheduledTaskCommandPort {

    private final IScheduledTaskCommandRepositoryPort scheduledTaskCommandRepositoryPort;
    private final IFormatterResultOutputPort formatterResultOutputPort;
    private final IMessageServicePort messageServicePort;

    @Override
    public ScheduledTask scheduleTask(ScheduledTask scheduledTask) {
        if (scheduledTask == null) {
            this.formatterResultOutputPort.returnBusinessRuleErrorResponse(
                    400,
                    this.messageServicePort.getMessage(MessageKeys.ERROR_NULL_VALUE, "ScheduledTask"));
        }
        validateTaskPayload(scheduledTask);
        prepareDefaultValues(scheduledTask);
        ScheduledTask persistedTask = this.scheduledTaskCommandRepositoryPort.save(scheduledTask);
        log.info("Scheduled task {} of type {} for {}", persistedTask.getPublicId(),
                persistedTask.getTaskType(), persistedTask.getExecuteAtInstant());
        return persistedTask;
    }

    private void validateTaskPayload(ScheduledTask scheduledTask) {
        if (scheduledTask.getTaskType() == null) {
            this.formatterResultOutputPort.returnBusinessRuleErrorResponse(
                    400,
                    this.messageServicePort.getMessage(MessageKeys.VALIDATION_FIELD_REQUIRED, "taskType"));
        }
        if (scheduledTask.getExecuteAt() == null || scheduledTask.getExecuteAtInstant() == null) {
            this.formatterResultOutputPort.returnBusinessRuleErrorResponse(
                    400,
                    this.messageServicePort.getMessage(MessageKeys.VALIDATION_FIELD_REQUIRED, "executeAt"));
        }
        if (scheduledTask.getPayload() == null || scheduledTask.getPayload().isEmpty()) {
            this.formatterResultOutputPort.returnBusinessRuleErrorResponse(
                    400,
                    this.messageServicePort.getMessage(MessageKeys.VALIDATION_FIELD_REQUIRED, "payload"));
        }
        try {
            scheduledTask.getTaskType().validatePayload(scheduledTask.getPayload());
        } catch (IllegalArgumentException ex) {
            this.formatterResultOutputPort.returnBusinessRuleErrorResponse(
                    400,
                    this.messageServicePort.getMessage(MessageKeys.ERROR_INVALID_VALUE, ex.getMessage()));
        }
        try {
            TaskExecutionTime executionTime = TaskExecutionTime.scheduleAt(
                    scheduledTask.getExecuteAtInstant(), Instant.now());
            scheduledTask.setExecuteAt(executionTime);
        } catch (IllegalArgumentException ex) {
            this.formatterResultOutputPort.returnBusinessRuleErrorResponse(
                    400,
                    this.messageServicePort.getMessage(MessageKeys.ERROR_INVALID_VALUE, ex.getMessage()));
        }
    }

    private void prepareDefaultValues(ScheduledTask scheduledTask) {
        if (scheduledTask.getPublicId() == null) {
            scheduledTask.setPublicId(UUID.randomUUID().toString());
        }
        scheduledTask.markPending();
        scheduledTask.setAttempts(0);
        scheduledTask.setArtifactPath(null);
        scheduledTask.setLastExecutionAt(null);
        scheduledTask.setLastError(null);
    }
}
