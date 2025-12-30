package com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks;

/**
 * Lifecycle of scheduled tasks.
 */
public enum ScheduledTaskStatus {
    PENDING,
    IN_PROGRESS,
    EXECUTED,
    FAILED;

    public boolean canTransitionTo(ScheduledTaskStatus target) {
        return switch (this) {
            case PENDING -> target == IN_PROGRESS || target == FAILED;
            case IN_PROGRESS -> target == EXECUTED || target == FAILED;
            case FAILED -> target == PENDING || target == IN_PROGRESS;
            case EXECUTED -> false;
        };
    }
}
