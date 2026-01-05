package com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Types of tasks supported by the scheduler.
 */
public enum ScheduledTaskType {
    SEND_EMAIL(Set.of("emailTo", "subject", "body")),
    GENERATE_REPORT(Set.of("reportType", "criteria")),
    SCHEDULED_DOWNLOAD(Set.of("resourceId")),
    AUXILIARY_BOOK_EMAIL(Set.of("deliveryWay", "format", "entName", "auxiliaryBook", "template", "emailTo")),
    AUXILIARY_BOOK_DOWNLOAD(Set.of("deliveryWay", "format", "entName", "auxiliaryBook", "template"));

    private final Set<String> requiredPayloadKeys;

    ScheduledTaskType(Set<String> requiredPayloadKeys) {
        this.requiredPayloadKeys = requiredPayloadKeys;
    }

    public void validatePayload(Map<String, Object> payload) {
        Objects.requireNonNull(payload, "Payload is required");
        for (String key : requiredPayloadKeys) {
            if (!payload.containsKey(key) || payload.get(key) == null) {
                throw new IllegalArgumentException("Missing payload attribute '" + key + "' for task type " + this);
            }
        }
    }

    public Set<String> getRequiredPayloadKeys() {
        return Collections.unmodifiableSet(requiredPayloadKeys);
    }
}
