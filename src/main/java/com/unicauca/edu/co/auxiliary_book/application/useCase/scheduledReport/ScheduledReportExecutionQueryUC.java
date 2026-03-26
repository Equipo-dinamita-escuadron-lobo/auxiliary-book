package com.unicauca.edu.co.auxiliary_book.application.useCase.scheduledReport;

import com.unicauca.edu.co.auxiliary_book.application.ports.in.scheduledReport.IScheduledReportExecutionsQueryPort;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledJobExecution.ReportExecution;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduling.ScheduledAuxiliaryBookJob;
import com.unicauca.edu.co.auxiliary_book.domain.ports.IFormatterResultOutputPort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.IMessageServicePort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.scheduledReport.IScheduledReportExecutionQueryRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.scheduledReport.IScheduledReportQueryRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.infrastructure.config.i18n.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduledReportExecutionQueryUC implements IScheduledReportExecutionsQueryPort {

    private final IScheduledReportExecutionQueryRepositoryPort scheduledReportExecutionQueryRepositoryPort;
    private final IScheduledReportQueryRepositoryPort scheduledReportQueryRepositoryPort;
    private final IFormatterResultOutputPort formatterResultOutputPort;
    private final IMessageServicePort messageServicePort;

    @Override
    public List<ReportExecution> listExecutionsByJob(String scheduledReportPublicId, String filters) {
        if (scheduledReportPublicId == null || scheduledReportPublicId.isBlank()) {
            this.formatterResultOutputPort.returnBusinessRuleErrorResponse(400,
                    this.messageServicePort.getMessage(MessageKeys.VALIDATION_FIELD_REQUIRED, "scheduledReportPublicId"));
        }

        ScheduledAuxiliaryBookJob job = this.scheduledReportQueryRepositoryPort.findByPublicId(scheduledReportPublicId)
                .orElseGet(() -> {
                    this.formatterResultOutputPort.returnEntityDoesNotExistErrorResponse(404,
                            this.messageServicePort.getMessage(MessageKeys.ERROR_NOT_FOUND, "ScheduledReport"));
                    return null;
                });

        try {
            UUID jobUuid = UUID.fromString(job.getPublicId());
            return this.scheduledReportExecutionQueryRepositoryPort.findByJobId(jobUuid, filters);
        } catch (IllegalArgumentException ex) {
            this.formatterResultOutputPort.returnBusinessRuleErrorResponse(400,
                    this.messageServicePort.getMessage(MessageKeys.ERROR_INVALID_VALUE, "publicId"));
            return List.of();
        }
    }
}
