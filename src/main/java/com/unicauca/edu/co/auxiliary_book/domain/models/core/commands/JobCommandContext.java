package com.unicauca.edu.co.auxiliary_book.domain.models.core.commands;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class JobCommandContext {
    private UUID jobId;
    private UUID executionId;
    private Instant scheduledAt;
    private String correlationId;
    private String triggeredBy;
    private Map<String,Object> attributes;
}
