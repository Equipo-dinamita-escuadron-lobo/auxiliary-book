package com.unicauca.edu.co.auxiliary_book.domain.ports.scheduledReport;

import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EJobStatus;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduling.ScheduledAuxiliaryBookJob;

public interface IScheduledReportCommandRepositoryPort {
    ScheduledAuxiliaryBookJob save(ScheduledAuxiliaryBookJob job);
    void updateStatus(Long jobId, EJobStatus status);
}
