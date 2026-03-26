package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.repository.scheduledReport;

import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.scheduledReport.ScheduledReportExecutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IScheduledReportExecutionRepository extends JpaRepository<ScheduledReportExecutionEntity, UUID> {
    Optional<ScheduledReportExecutionEntity> findByJobIdAndScheduledAt(UUID jobId, Instant scheduledAt);

    List<ScheduledReportExecutionEntity> findByJobId(UUID jobId);
}
