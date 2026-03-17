package com.unicauca.edu.co.auxiliary_book.domain.ports.scheduledReport;

import com.unicauca.edu.co.auxiliary_book.domain.models.scheduling.ScheduledAuxiliaryBookJob;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IScheduledReportQueryRepositoryPort {
    Optional<ScheduledAuxiliaryBookJob> findById(UUID jobId);
    List<ScheduledAuxiliaryBookJob> findDueJobs(Instant now, int limit);
    //List<ScheduledAuxiliaryBookJob> list();
}
