package com.unicauca.edu.co.auxiliary_book.infrastructure.out.scheduler;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.unicauca.edu.co.auxiliary_book.application.useCase.scheduledTask.ExecuteScheduledTasksUseCase;
import com.unicauca.edu.co.auxiliary_book.application.useCase.scheduledTask.FindPendingTasksUseCase;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTask;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DynamicTaskScheduler {

    private final FindPendingTasksUseCase findPendingTasksUseCase;
    private final ExecuteScheduledTasksUseCase executeScheduledTasksUseCase;

    @Value("${scheduled.tasks.batch-size:25}")
    private int batchSize;

    @Scheduled(fixedDelayString = "${scheduled.tasks.poll-interval-ms:15000}")
    public void dispatchDueTasks() {
        Instant now = Instant.now();
        List<ScheduledTask> dueTasks = this.findPendingTasksUseCase.findDueTasks(now, batchSize);
        dueTasks.forEach(task -> {
            log.debug("Dispatching scheduled task {}", task.getPublicId());
            this.executeScheduledTasksUseCase.executeTask(task);
        });
    }
}
