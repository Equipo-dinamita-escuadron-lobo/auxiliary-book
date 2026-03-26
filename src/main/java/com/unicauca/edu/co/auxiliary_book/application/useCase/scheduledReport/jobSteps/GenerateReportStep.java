package com.unicauca.edu.co.auxiliary_book.application.useCase.scheduledReport.jobSteps;

import com.unicauca.edu.co.auxiliary_book.application.ports.in.auxiliaryBook.IAuxiliaryBookCommandPort;
import com.unicauca.edu.co.auxiliary_book.application.ports.in.export.IExportReportPort;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.commands.IJobCommand;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.commands.JobCommandContext;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.export.AuxiliaryBookTemplate;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.export.ExportInfo;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookFormat;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduling.ScheduledAuxiliaryBookJob;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(10)
@RequiredArgsConstructor
public class GenerateReportStep implements IJobCommand {

    private final IAuxiliaryBookCommandPort auxiliaryBookCommandPort;
    private final IExportReportPort exportReportPort;

    @Override
    public void execute(JobCommandContext context) {
        ScheduledAuxiliaryBookJob job = context.getRequiredAttribute(JobCommandContext.ATTRIBUTE_JOB, ScheduledAuxiliaryBookJob.class);
        EAuxiliaryBookFormat reportFormat = resolveReportFormat(context);

        AuxiliaryBook book = AuxiliaryBook.builder()
                .type(job.getBookType())
                .entId(job.getEntId())
                .userId(job.getUserId())
                .format(reportFormat)
                .criteria(job.getCriteria())
                .build();

        AuxiliaryBook registeredBook = auxiliaryBookCommandPort.registerAuxiliaryBook(book);
        List<?> reportData = auxiliaryBookCommandPort.genereteAuxiliaryBookInfo(registeredBook);

        ExportInfo exportInfo = new ExportInfo(
                reportFormat,
                job.getEntId(),
                registeredBook,
                reportData,
                buildDefaultTemplate(job)
        );

        byte[] reportBytes = exportReportPort.exportReport(exportInfo);
        if (reportBytes == null || reportBytes.length == 0) {
            throw new IllegalStateException("Exported report content is empty.");
        }

        context.putAttribute(JobCommandContext.ATTRIBUTE_REGISTERED_BOOK, registeredBook);
        context.putAttribute(JobCommandContext.ATTRIBUTE_REPORT_DATA, reportData);
        context.putAttribute(JobCommandContext.ATTRIBUTE_REPORT_BYTES, reportBytes);
    }

    private EAuxiliaryBookFormat resolveReportFormat(JobCommandContext context) {
        EAuxiliaryBookFormat reportFormat = context.getAttribute(JobCommandContext.ATTRIBUTE_REPORT_FORMAT, EAuxiliaryBookFormat.class);
        return reportFormat != null ? reportFormat : EAuxiliaryBookFormat.PDF;
    }

    private AuxiliaryBookTemplate buildDefaultTemplate(ScheduledAuxiliaryBookJob job) {
        String templateName = job != null && job.getBookType() != null
                ? job.getBookType().name()
                : "Auxiliary Book";

        return AuxiliaryBookTemplate.builder()
                .name(templateName)
                .font("Arial")
                .mainColor("#0B3C61")
                .build();
    }
}
