package com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks;

import java.time.Instant;
import java.util.Objects;

/**
 * Value object encapsulating the execution instant of a task.
 */
public final class TaskExecutionTime {

    private final Instant value;

    private TaskExecutionTime(Instant value) {
        this.value = value;
    }

    public static TaskExecutionTime scheduleAt(Instant executeAt, Instant now) {
        Objects.requireNonNull(executeAt, "Execution date is required");
        Objects.requireNonNull(now, "Reference date is required");
        if (executeAt.isBefore(now)) {
            throw new IllegalArgumentException("Execution date must be in the future");
        }
        return new TaskExecutionTime(executeAt);
    }

    public static TaskExecutionTime from(Instant executeAt) {
        Objects.requireNonNull(executeAt, "Execution date is required");
        return new TaskExecutionTime(executeAt);
    }

    public boolean isDue(Instant reference) {
        return !value.isAfter(reference);
    }

    public Instant getValue() {
        return value;
    }
}
