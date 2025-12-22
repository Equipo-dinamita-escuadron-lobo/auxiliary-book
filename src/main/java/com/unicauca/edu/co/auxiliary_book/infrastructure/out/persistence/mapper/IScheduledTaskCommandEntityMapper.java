package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.mapper;

import java.time.Instant;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTask;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.TaskExecutionTime;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.ScheduledTaskEntity;

@Mapper(componentModel = "spring")
public interface IScheduledTaskCommandEntityMapper {

    @Mapping(target = "payloadJson", ignore = true)
    @Mapping(target = "lockedUntil", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "executeAt", expression = "java(toInstant(scheduledTask.getExecuteAt()))")
    ScheduledTaskEntity toEntity(ScheduledTask scheduledTask);

    @Mapping(target = "payload", ignore = true)
    @Mapping(target = "executeAt", expression = "java(toExecutionTime(scheduledTaskEntity.getExecuteAt()))")
    ScheduledTask toDomain(ScheduledTaskEntity scheduledTaskEntity);

    default Instant toInstant(TaskExecutionTime executionTime) {
        return executionTime == null ? null : executionTime.getValue();
    }

    default TaskExecutionTime toExecutionTime(Instant instant) {
        return instant == null ? null : TaskExecutionTime.from(instant);
    }
}
