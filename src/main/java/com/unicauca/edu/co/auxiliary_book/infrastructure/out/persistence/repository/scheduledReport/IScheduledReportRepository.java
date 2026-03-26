package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.repository.scheduledReport;

import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EJobStatus;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.scheduledReport.ScheduledReportEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface IScheduledReportRepository extends JpaRepository<ScheduledReportEntity, Long> {
    Optional<ScheduledReportEntity> findByPublicId(String publicId);

    List<ScheduledReportEntity> findByEntId(String entId);

    List<ScheduledReportEntity> findByStatusAndNextRunAtLessThanEqualOrderByNextRunAtAsc(EJobStatus status, Instant now, Pageable pageable);
}
