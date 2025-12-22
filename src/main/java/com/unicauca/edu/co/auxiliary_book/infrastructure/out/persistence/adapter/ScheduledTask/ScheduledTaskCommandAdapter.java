package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.adapter.ScheduledTask;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTask;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTaskStatus;
import com.unicauca.edu.co.auxiliary_book.domain.ports.ScheduledTasks.IScheduledTaskCommandRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.ScheduledTaskEntity;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.mapper.IScheduledTaskCommandEntityMapper;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.mapper.IScheduledTaskQueryEntityMapper;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.repository.IScheduledTaskRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTaskCommandAdapter implements IScheduledTaskCommandRepositoryPort {

    private final IScheduledTaskRepository scheduledTaskRepository;
    private final IScheduledTaskCommandEntityMapper commandEntityMapper;
    private final IScheduledTaskQueryEntityMapper queryEntityMapper;
    private final ObjectMapper objectMapper;

    @Override
    public ScheduledTask save(ScheduledTask task) {
        ScheduledTaskEntity entity = this.commandEntityMapper.toEntity(task);
        entity.setPayloadJson(writePayload(task.getPayload()));
        ScheduledTaskEntity persisted = this.scheduledTaskRepository.save(entity);
        return mapToDomain(persisted);
    }

    @Override
    public ScheduledTask update(ScheduledTask task) {
        ScheduledTaskEntity entity = this.commandEntityMapper.toEntity(task);
        entity.setPayloadJson(writePayload(task.getPayload()));
        ScheduledTaskEntity persisted = this.scheduledTaskRepository.save(entity);
        return mapToDomain(persisted);
    }

    @Override
    public void updateStatus(Long taskId, ScheduledTaskStatus status, String artifactPath, String errorMessage,
                             Instant executedAt) {
        this.scheduledTaskRepository.findById(taskId).ifPresent(entity -> {
            entity.setStatus(status);
            entity.setArtifactPath(artifactPath);
            entity.setLastError(errorMessage);
            entity.setLastExecutionAt(executedAt);
            this.scheduledTaskRepository.save(entity);
        });
    }

    @Override
    @Transactional
    public boolean acquireExecutionLock(Long taskId, Instant lockUntil) {
        Instant reference = Instant.now();
        int updatedRows = this.scheduledTaskRepository.acquireLock(taskId, reference, lockUntil);
        return updatedRows > 0;
    }

    private ScheduledTask mapToDomain(ScheduledTaskEntity entity) {
        ScheduledTask task = this.queryEntityMapper.toDomain(entity);
        task.setPayload(readPayload(entity.getPayloadJson()));
        return task;
    }

    private String writePayload(Map<String, Object> payload) {
        try {
            return this.objectMapper.writeValueAsString(payload == null ? Collections.emptyMap() : payload);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to serialize task payload", e);
        }
    }

    private Map<String, Object> readPayload(String payloadJson) {
        if (payloadJson == null || payloadJson.isBlank()) {
            return Collections.emptyMap();
        }
        try {
            return this.objectMapper.readValue(payloadJson, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            log.error("Unable to deserialize payload", e);
            return Collections.emptyMap();
        }
    }
}
