package com.unicauca.edu.co.auxiliary_book.application.useCase.scheduledReport.jobSteps;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.commands.IJobCommand;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.commands.JobCommandContext;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EDeliveryStatus;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledJobExecution.ReportExecution;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(20)
public class PrepareDownloadStep implements IJobCommand {

    @Override
    public void execute(JobCommandContext context) {
        ReportExecution execution = context.getRequiredAttribute(JobCommandContext.ATTRIBUTE_EXECUTION, ReportExecution.class);
        boolean downloadEnabled = Boolean.TRUE.equals(
                context.getAttribute(JobCommandContext.ATTRIBUTE_DOWNLOAD_ENABLED, Boolean.class)
        );
        boolean emailEnabled = Boolean.TRUE.equals(
                context.getAttribute(JobCommandContext.ATTRIBUTE_EMAIL_ENABLED, Boolean.class)
        );

        if (!downloadEnabled && !emailEnabled) {
            execution.setDeliveryStatus(EDeliveryStatus.FAILED);
            execution.setErrorMessage("No delivery method configured for scheduled report.");
            return;
        }

        if (downloadEnabled) {
            execution.setDeliveryStatus(EDeliveryStatus.READY_FOR_DOWNLOAD);
        }
    }
}
