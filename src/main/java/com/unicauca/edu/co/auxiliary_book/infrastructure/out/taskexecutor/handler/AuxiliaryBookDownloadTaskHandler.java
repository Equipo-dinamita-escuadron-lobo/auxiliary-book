package com.unicauca.edu.co.auxiliary_book.infrastructure.out.taskexecutor.handler;

import org.springframework.stereotype.Component;

import com.unicauca.edu.co.auxiliary_book.application.useCase.scheduledTask.AuxiliaryBookScheduledExecutor;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTask;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTaskType;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.TaskExecutionResult;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuxiliaryBookDownloadTaskHandler implements ScheduledTaskHandler {

    private final AuxiliaryBookScheduledExecutor executor;

    @Override
    public ScheduledTaskType supports() {
        return ScheduledTaskType.AUXILIARY_BOOK_DOWNLOAD;
    }

    @Override
    public TaskExecutionResult handle(ScheduledTask task) {
        return this.executor.execute(task, ScheduledTaskType.AUXILIARY_BOOK_DOWNLOAD);
    }
}
