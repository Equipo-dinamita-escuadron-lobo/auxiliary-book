package com.unicauca.edu.co.auxiliary_book.infrastructure.out.taskexecutor;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTask;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTaskType;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.TaskExecutionResult;
import com.unicauca.edu.co.auxiliary_book.domain.ports.ScheduledTasks.ITaskExecutorPort;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.taskexecutor.handler.ScheduledTaskHandler;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TaskExecutorAdapter implements ITaskExecutorPort {

    private final Map<ScheduledTaskType, ScheduledTaskHandler> handlers = new EnumMap<>(ScheduledTaskType.class);

    public TaskExecutorAdapter(List<ScheduledTaskHandler> registeredHandlers) {
        registeredHandlers.forEach(handler -> this.handlers.put(handler.supports(), handler));
    }

    @Override
    public TaskExecutionResult execute(ScheduledTask task) {
        ScheduledTaskHandler handler = this.handlers.get(task.getTaskType());
        if (handler == null) {
            log.warn("No task handler registered for type {}", task.getTaskType());
            return TaskExecutionResult.failure("Task handler not implemented");
        }
        return handler.handle(task);
    }
}
