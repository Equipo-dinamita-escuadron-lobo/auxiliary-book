package com.unicauca.edu.co.auxiliary_book.application.ports.in.scheduledReport;

import com.unicauca.edu.co.auxiliary_book.domain.models.scheduling.ScheduledAuxiliaryBookJob;

import java.util.List;

public interface IScheduledReportQueryPort {
    List<ScheduledAuxiliaryBookJob> listScheduledReports(String entId);

    ScheduledAuxiliaryBookJob getScheduledReport(String publicId);
}
