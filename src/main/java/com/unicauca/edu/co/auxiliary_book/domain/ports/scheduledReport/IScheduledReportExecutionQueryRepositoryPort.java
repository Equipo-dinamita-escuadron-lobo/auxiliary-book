package com.unicauca.edu.co.auxiliary_book.domain.ports.scheduledReport;

import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledJobExecution.ReportExecution;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IScheduledReportExecutionQueryRepositoryPort {
    Optional<ReportExecution> findById(UUID executionId);
    List<ReportExecution> findByJobId(UUID jobId, String filtros/* filtros */);
    Optional<ReportExecution> findByJobIdAndScheduledAt(UUID jobId, Instant scheduledAt);
}
