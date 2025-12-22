package com.unicauca.edu.co.auxiliary_book.application.ports.in.scheduledTasks;

import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTask;

public interface IScheduledTaskCommandPort {
    ScheduledTask scheduleTask(ScheduledTask scheduledTask);
}
