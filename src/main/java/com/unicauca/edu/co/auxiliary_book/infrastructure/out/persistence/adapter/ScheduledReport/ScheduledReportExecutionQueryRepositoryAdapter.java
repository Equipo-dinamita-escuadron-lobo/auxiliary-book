package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.adapter.ScheduledReport;

import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledJobExecution.ReportExecution;
import com.unicauca.edu.co.auxiliary_book.domain.ports.scheduledReport.IScheduledReportExecutionQueryRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.scheduledReport.ScheduledReportExecutionEntity;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.repository.scheduledReport.IScheduledReportExecutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ScheduledReportExecutionQueryRepositoryAdapter implements IScheduledReportExecutionQueryRepositoryPort {

    private final IScheduledReportExecutionRepository scheduledReportExecutionRepository;

    @Override
    public Optional<ReportExecution> findById(UUID executionId) {
        return scheduledReportExecutionRepository.findById(executionId).map(this::toDomain);
    }

    @Override
    public List<ReportExecution> findByJobId(UUID jobId, String filtros) {
        return scheduledReportExecutionRepository.findByJobId(jobId).stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<ReportExecution> findByJobIdAndScheduledAt(UUID jobId, Instant scheduledAt) {
        return scheduledReportExecutionRepository.findByJobIdAndScheduledAt(jobId, scheduledAt).map(this::toDomain);
    }

    private ReportExecution toDomain(ScheduledReportExecutionEntity entity) {
        ReportExecution execution = new ReportExecution();
        execution.setExecutionId(entity.getExecutionId());
        execution.setJobId(entity.getJobId());
        execution.setScheduledAt(entity.getScheduledAt());
        execution.setStartedAt(entity.getStartedAt());
        execution.setFinishedAt(entity.getFinishedAt());
        execution.setStatusExecution(entity.getStatusExecution());
        execution.setDeliveryStatus(entity.getDeliveryStatus());
        execution.setErrorCode(entity.getErrorCode());
        execution.setErrorMessage(entity.getErrorMessage());
        execution.setCorrelationId(entity.getCorrelationId());
        execution.setRetryCount(entity.getRetryCount());
        return execution;
    }
}
