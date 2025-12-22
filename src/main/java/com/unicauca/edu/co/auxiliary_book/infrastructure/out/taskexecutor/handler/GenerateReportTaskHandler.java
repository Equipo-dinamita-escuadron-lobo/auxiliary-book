package com.unicauca.edu.co.auxiliary_book.infrastructure.out.taskexecutor.handler;

import org.springframework.stereotype.Component;

import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTask;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTaskType;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.TaskExecutionResult;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GenerateReportTaskHandler implements ScheduledTaskHandler {

    @Override
    public ScheduledTaskType supports() {
        return ScheduledTaskType.GENERATE_REPORT;
    }

    @Override
    public TaskExecutionResult handle(ScheduledTask task) {
        log.info("Generating report for task {} with payload {}", task.getPublicId(), task.getPayload());
        // TODO integrate with report generation service
        return TaskExecutionResult.success(null);
    }
}
