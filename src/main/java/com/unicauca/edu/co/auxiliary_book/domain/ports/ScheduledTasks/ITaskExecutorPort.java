package com.unicauca.edu.co.auxiliary_book.domain.ports.ScheduledTasks;

import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTask;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.TaskExecutionResult;

/**
 * Output port used by the application layer to execute scheduled tasks.
 */
public interface ITaskExecutorPort {

    TaskExecutionResult execute(ScheduledTask task);
}
