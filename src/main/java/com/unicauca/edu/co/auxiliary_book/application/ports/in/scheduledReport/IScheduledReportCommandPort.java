package com.unicauca.edu.co.auxiliary_book.application.ports.in.scheduledReport;

import com.unicauca.edu.co.auxiliary_book.domain.models.scheduling.ScheduledAuxiliaryBookJob;

public interface IScheduledReportCommandPort {
    ScheduledAuxiliaryBookJob createScheduledReport(ScheduledAuxiliaryBookJob job);

    ScheduledAuxiliaryBookJob updateScheduledReport(String publicId, ScheduledAuxiliaryBookJob job);

    void cancelScheduledReport(String publicId);
}
