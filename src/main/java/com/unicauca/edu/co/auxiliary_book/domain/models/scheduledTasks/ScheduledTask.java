package com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Aggregate root representing a scheduled task inside the domain.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledTask {

    private Long id;
    private String publicId;
    private ScheduledTaskType taskType;
    private ScheduledTaskStatus status;
    private TaskExecutionTime executeAt;
    @Builder.Default
    private Map<String, Object> payload = new HashMap<>();
    private String artifactPath;
    private Instant lastExecutionAt;
    private String lastError;
    @Builder.Default
    private Integer attempts = 0;

    public Map<String, Object> getPayload() {
        return Collections.unmodifiableMap(payload);
    }

    public void setPayload(Map<String, Object> payload) {
        if (payload == null) {
            this.payload = new HashMap<>();
            return;
        }
        this.payload = new HashMap<>(payload);
    }

    public Instant getExecuteAtInstant() {
        return executeAt != null ? executeAt.getValue() : null;
    }

    public void markPending() {
        this.status = ScheduledTaskStatus.PENDING;
        this.lastError = null;
    }

    public void markInProgress() {
        ensureStatusTransition(ScheduledTaskStatus.IN_PROGRESS);
        this.attempts = Objects.requireNonNullElse(this.attempts, 0) + 1;
    }

    public void markExecuted(String artifactPath, Instant executedAt) {
        ensureStatusTransition(ScheduledTaskStatus.EXECUTED);
        this.artifactPath = artifactPath;
        this.lastExecutionAt = executedAt;
        this.lastError = null;
    }

    public void markFailed(String errorMessage, Instant executedAt) {
        ensureStatusTransition(ScheduledTaskStatus.FAILED);
        this.lastExecutionAt = executedAt;
        this.lastError = errorMessage;
    }

    public boolean isDue(Instant reference) {
        return executeAt != null && executeAt.isDue(reference);
    }

    private void ensureStatusTransition(ScheduledTaskStatus nextStatus) {
        if (status == null) {
            status = ScheduledTaskStatus.PENDING;
        }
        if (!status.canTransitionTo(nextStatus)) {
            throw new IllegalStateException(
                    "Invalid state transition from " + status + " to " + nextStatus);
        }
        this.status = nextStatus;
    }
}
