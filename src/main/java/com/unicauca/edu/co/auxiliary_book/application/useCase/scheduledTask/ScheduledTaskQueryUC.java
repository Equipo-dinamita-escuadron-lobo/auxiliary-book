package com.unicauca.edu.co.auxiliary_book.application.useCase.scheduledTask;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.unicauca.edu.co.auxiliary_book.application.ports.in.scheduledTasks.IScheduledTaskQueryPort;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTask;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTaskStatus;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTaskType;
import com.unicauca.edu.co.auxiliary_book.domain.ports.IFormatterResultOutputPort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.IMessageServicePort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.ScheduledTasks.IScheduledTaskQueryRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.infrastructure.config.i18n.MessageKeys;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduledTaskQueryUC implements IScheduledTaskQueryPort {

    private final IScheduledTaskQueryRepositoryPort scheduledTaskQueryRepositoryPort;
    private final IFormatterResultOutputPort formatterResultOutputPort;
    private final IMessageServicePort messageServicePort;

    @Override
    public Page<ScheduledTask> findByFilters(ScheduledTaskStatus status, ScheduledTaskType type, Pageable pageable) {
        return this.scheduledTaskQueryRepositoryPort.findByFilters(status, type, pageable);
    }

    @Override
    public ScheduledTask findByPublicId(String publicId) {
        if (publicId == null || publicId.isBlank()) {
            this.formatterResultOutputPort.returnBusinessRuleErrorResponse(
                    400,
                    this.messageServicePort.getMessage(MessageKeys.VALIDATION_FIELD_REQUIRED, "taskId"));
        }
        return this.scheduledTaskQueryRepositoryPort.findByPublicId(publicId)
                .orElseGet(() -> {
                    this.formatterResultOutputPort.returnEntityDoesNotExistErrorResponse(
                            404,
                            this.messageServicePort.getMessage(MessageKeys.ERROR_NOT_FOUND, publicId));
                    return null;
                });
    }
}
