package com.unicauca.edu.co.auxiliary_book.application.ports.in.scheduledTasks;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTask;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTaskStatus;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTaskType;

public interface IScheduledTaskQueryPort {

    Page<ScheduledTask> findByFilters(ScheduledTaskStatus status, ScheduledTaskType type, Pageable pageable);

    ScheduledTask findByPublicId(String publicId);
}
