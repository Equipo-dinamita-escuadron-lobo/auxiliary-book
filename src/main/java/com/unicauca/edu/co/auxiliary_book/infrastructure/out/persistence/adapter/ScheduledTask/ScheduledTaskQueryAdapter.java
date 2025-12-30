package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.adapter.ScheduledTask;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTask;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTaskStatus;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTaskType;
import com.unicauca.edu.co.auxiliary_book.domain.ports.ScheduledTasks.IScheduledTaskQueryRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.ScheduledTaskEntity;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.mapper.IScheduledTaskQueryEntityMapper;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.repository.IScheduledTaskRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ScheduledTaskQueryAdapter implements IScheduledTaskQueryRepositoryPort {

    private final IScheduledTaskRepository scheduledTaskRepository;
    private final IScheduledTaskQueryEntityMapper scheduledTaskQueryEntityMapper;
    private final ObjectMapper objectMapper;

    @Override
    public Optional<ScheduledTask> findByPublicId(String publicId) {
        return this.scheduledTaskRepository.findByPublicId(publicId).map(this::mapToDomain);
    }

    @Override
    public Optional<ScheduledTask> findById(Long id) {
        return this.scheduledTaskRepository.findById(id).map(this::mapToDomain);
    }

    @Override
    public Page<ScheduledTask> findByFilters(ScheduledTaskStatus status, ScheduledTaskType type, Pageable pageable) {
        Page<ScheduledTaskEntity> entities = this.scheduledTaskRepository.findByFilters(status, type, pageable);
        List<ScheduledTask> domainList = entities.getContent().stream()
                .map(this::mapToDomain)
                .toList();
        return new PageImpl<>(domainList, entities.getPageable(), entities.getTotalElements());
    }

    @Override
    public List<ScheduledTask> findPendingToExecute(Instant reference, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<ScheduledTaskEntity> entities = this.scheduledTaskRepository
                .findPendingForExecution(ScheduledTaskStatus.PENDING, reference, pageable);
        return entities.stream().map(this::mapToDomain).toList();
    }

    private ScheduledTask mapToDomain(ScheduledTaskEntity entity) {
        ScheduledTask task = this.scheduledTaskQueryEntityMapper.toDomain(entity);
        task.setPayload(readPayload(entity.getPayloadJson()));
        return task;
    }

    private Map<String, Object> readPayload(String json) {
        if (json == null || json.isBlank()) {
            return Collections.emptyMap();
        }
        try {
            return this.objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception ex) {
            return Collections.emptyMap();
        }
    }
}
