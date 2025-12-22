package com.unicauca.edu.co.auxiliary_book.application.useCase.scheduledTask;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTask;
import com.unicauca.edu.co.auxiliary_book.domain.ports.ScheduledTasks.IScheduledTaskQueryRepositoryPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FindPendingTasksUseCase {

    private final IScheduledTaskQueryRepositoryPort scheduledTaskQueryRepositoryPort;

    public List<ScheduledTask> findDueTasks(Instant reference, int limit) {
        Instant ref = reference != null ? reference : Instant.now();
        int pageSize = limit > 0 ? limit : 50;
        return this.scheduledTaskQueryRepositoryPort.findPendingToExecute(ref, pageSize);
    }
}
