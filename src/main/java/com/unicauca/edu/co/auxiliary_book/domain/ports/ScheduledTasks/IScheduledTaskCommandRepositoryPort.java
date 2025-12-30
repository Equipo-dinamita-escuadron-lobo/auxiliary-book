package com.unicauca.edu.co.auxiliary_book.domain.ports.ScheduledTasks;

import java.time.Instant;

import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTask;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTaskStatus;

/**
 * Output port for scheduled task write operations.
 */
public interface IScheduledTaskCommandRepositoryPort {

    ScheduledTask save(ScheduledTask task);

    ScheduledTask update(ScheduledTask task);

    void updateStatus(Long taskId, ScheduledTaskStatus status, String artifactPath, String errorMessage,
                      Instant executedAt);

    boolean acquireExecutionLock(Long taskId, Instant lockUntil);
}
