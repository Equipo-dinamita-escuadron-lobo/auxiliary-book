package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.mapper;

import java.time.Instant;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTask;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.TaskExecutionTime;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.request.ScheduleTaskRequest;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.response.ScheduledTaskResponseDTO;

@Component
public class ScheduledTaskRestMapper {

    public ScheduledTask toDomain(ScheduleTaskRequest request) {
        ScheduledTask scheduledTask = new ScheduledTask();
        scheduledTask.setTaskType(request.taskType());
        scheduledTask.setExecuteAt(TaskExecutionTime.from(request.executeAt().toInstant()));
        scheduledTask.setPayload(request.payload());
        return scheduledTask;
    }

    public ScheduledTaskResponseDTO toResponse(ScheduledTask task) {
        Instant executeAt = task.getExecuteAtInstant();
        Map<String, Object> payload = task.getPayload();
        return ScheduledTaskResponseDTO.builder()
                .publicId(task.getPublicId())
                .taskType(task.getTaskType())
                .status(task.getStatus())
                .executeAt(executeAt)
                .payload(payload)
                .artifactPath(task.getArtifactPath())
                .lastExecutionAt(task.getLastExecutionAt())
                .lastError(task.getLastError())
                .attempts(task.getAttempts())
                .build();
    }
}
