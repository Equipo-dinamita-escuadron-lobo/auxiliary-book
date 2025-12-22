package com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks;

/**
 * Result returned by the TaskExecutor port after executing a task.
 */
public record TaskExecutionResult(boolean success, String artifactPath, String errorMessage) {

    public static TaskExecutionResult success(String artifactPath) {
        return new TaskExecutionResult(true, artifactPath, null);
    }

    public static TaskExecutionResult failure(String errorMessage) {
        return new TaskExecutionResult(false, null, errorMessage);
    }
}
