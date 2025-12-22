package com.unicauca.edu.co.auxiliary_book.infrastructure.out.taskexecutor.handler;

import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTask;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTaskType;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.TaskExecutionResult;

public interface ScheduledTaskHandler {

    ScheduledTaskType supports();

    TaskExecutionResult handle(ScheduledTask task);
}
