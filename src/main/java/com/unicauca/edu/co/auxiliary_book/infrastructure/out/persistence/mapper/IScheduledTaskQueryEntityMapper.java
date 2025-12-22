package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.mapper;

import java.time.Instant;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTask;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.TaskExecutionTime;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.ScheduledTaskEntity;

@Mapper(componentModel = "spring")
public interface IScheduledTaskQueryEntityMapper {

    @Mapping(target = "payload", ignore = true)
    @Mapping(target = "executeAt", expression = "java(toExecutionTime(entity.getExecuteAt()))")
    ScheduledTask toDomain(ScheduledTaskEntity entity);

    default TaskExecutionTime toExecutionTime(Instant instant) {
        return instant == null ? null : TaskExecutionTime.from(instant);
    }
}
