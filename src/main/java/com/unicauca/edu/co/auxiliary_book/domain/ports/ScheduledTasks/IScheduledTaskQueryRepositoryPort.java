package com.unicauca.edu.co.auxiliary_book.domain.ports.ScheduledTasks;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTask;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTaskStatus;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTaskType;

/**
 * Output port for scheduled task read operations.
 */
public interface IScheduledTaskQueryRepositoryPort {

    Optional<ScheduledTask> findByPublicId(String publicId);

    Optional<ScheduledTask> findById(Long id);

    Page<ScheduledTask> findByFilters(ScheduledTaskStatus status, ScheduledTaskType type, Pageable pageable);

    List<ScheduledTask> findPendingToExecute(Instant reference, int limit);
}
