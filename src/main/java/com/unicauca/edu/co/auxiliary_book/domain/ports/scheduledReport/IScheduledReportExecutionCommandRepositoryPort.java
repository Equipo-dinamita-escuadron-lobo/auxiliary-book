package com.unicauca.edu.co.auxiliary_book.domain.ports.scheduledReport;

import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledJobExecution.ReportExecution;

public interface IScheduledReportExecutionCommandRepositoryPort {
    ReportExecution save(ReportExecution execution);
}
