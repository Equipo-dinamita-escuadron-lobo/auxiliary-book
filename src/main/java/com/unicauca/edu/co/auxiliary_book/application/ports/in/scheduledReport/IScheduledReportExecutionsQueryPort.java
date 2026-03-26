package com.unicauca.edu.co.auxiliary_book.application.ports.in.scheduledReport;

import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledJobExecution.ReportExecution;

import java.util.List;

public interface IScheduledReportExecutionsQueryPort {
    List<ReportExecution> listExecutionsByJob(String scheduledReportPublicId, String filters);
}
