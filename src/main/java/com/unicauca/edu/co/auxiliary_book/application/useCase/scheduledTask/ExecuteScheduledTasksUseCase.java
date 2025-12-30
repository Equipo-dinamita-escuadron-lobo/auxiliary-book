package com.unicauca.edu.co.auxiliary_book.application.useCase.scheduledTask;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTask;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.TaskExecutionResult;
import com.unicauca.edu.co.auxiliary_book.domain.ports.ScheduledTasks.IScheduledTaskCommandRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.ScheduledTasks.IScheduledTaskQueryRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.ScheduledTasks.ITaskExecutorPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExecuteScheduledTasksUseCase {

    private final IScheduledTaskCommandRepositoryPort scheduledTaskCommandRepositoryPort;
    private final IScheduledTaskQueryRepositoryPort scheduledTaskQueryRepositoryPort;
    private final ITaskExecutorPort taskExecutorPort;

    private static final long LOCK_WINDOW_SECONDS = 120L;

    public void executeTask(Long taskId) {
        this.scheduledTaskQueryRepositoryPort.findById(taskId).ifPresent(this::executeTask);
    }

    public void executePendingBatch(int limit) {
        List<ScheduledTask> tasks = this.scheduledTaskQueryRepositoryPort.findPendingToExecute(Instant.now(), limit);
        tasks.forEach(this::executeTask);
    }

    public void executeTask(ScheduledTask task) {
        Instant now = Instant.now();
        if (!task.isDue(now)) {
            log.debug("Task {} not due yet", task.getPublicId());
            return;
        }

        Instant lockUntil = now.plusSeconds(LOCK_WINDOW_SECONDS);
        if (!this.scheduledTaskCommandRepositoryPort.acquireExecutionLock(task.getId(), lockUntil)) {
            log.debug("Task {} is locked by another worker", task.getPublicId());
            return;
        }

        try {
            task.markInProgress();
            this.scheduledTaskCommandRepositoryPort.update(task);

            TaskExecutionResult result = this.taskExecutorPort.execute(task);
            Instant executionTime = Instant.now();
            if (result.success()) {
                task.markExecuted(result.artifactPath(), executionTime);
            } else {
                task.markFailed(result.errorMessage(), executionTime);
            }
            this.scheduledTaskCommandRepositoryPort.update(task);
        } catch (Exception ex) {
            log.error("Error executing scheduled task {}", task.getPublicId(), ex);
            task.markFailed(ex.getMessage(), Instant.now());
            this.scheduledTaskCommandRepositoryPort.update(task);
        }
    }
}
