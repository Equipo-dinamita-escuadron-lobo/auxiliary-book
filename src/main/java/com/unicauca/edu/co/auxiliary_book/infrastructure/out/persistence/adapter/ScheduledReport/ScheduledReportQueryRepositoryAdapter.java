package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.adapter.ScheduledReport;

import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EJobStatus;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduling.DeliveryConfig;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduling.EmailConfig;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduling.ScheduleSpec;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduling.ScheduledAuxiliaryBookJob;
import com.unicauca.edu.co.auxiliary_book.domain.ports.scheduledReport.IScheduledReportQueryRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.scheduledReport.ScheduledReportEntity;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.mapper.IAuxiliaryBookCommandEntityMapper;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.repository.scheduledReport.IScheduledReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ScheduledReportQueryRepositoryAdapter implements IScheduledReportQueryRepositoryPort {

    private final IScheduledReportRepository scheduledReportRepository;
    private final IAuxiliaryBookCommandEntityMapper auxiliaryBookCommandEntityMapper;

    @Override
    public Optional<ScheduledAuxiliaryBookJob> findById(Long jobId) {
        return scheduledReportRepository.findById(jobId).map(this::toDomain);
    }

    @Override
    public Optional<ScheduledAuxiliaryBookJob> findByPublicId(String publicId) {
        return scheduledReportRepository.findByPublicId(publicId).map(this::toDomain);
    }

    @Override
    public List<ScheduledAuxiliaryBookJob> findByEntId(String entId) {
        return scheduledReportRepository.findByEntId(entId).stream().map(this::toDomain).toList();
    }

    @Override
    public List<ScheduledAuxiliaryBookJob> findDueJobs(Instant now, int limit) {
        int resolvedLimit = limit > 0 ? limit : 50;
        return scheduledReportRepository
                .findByStatusAndNextRunAtLessThanEqualOrderByNextRunAtAsc(EJobStatus.ACTIVE, now, PageRequest.of(0, resolvedLimit))
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private ScheduledAuxiliaryBookJob toDomain(ScheduledReportEntity entity) {
        ScheduleSpec scheduleSpec = new ScheduleSpec(
                entity.getFrequency(),
                entity.getStartAt(),
                Optional.ofNullable(entity.getEndAt()),
                entity.getNextRunAt()
        );

        ScheduledAuxiliaryBookJob job = new ScheduledAuxiliaryBookJob();
        job.setJobId(entity.getId());
        job.setPublicId(entity.getPublicId());
        job.setBookType(entity.getBookType());
        job.setCriteria(auxiliaryBookCommandEntityMapper.toCriteria(entity.getCriteria()));
        job.setScheduleSpec(scheduleSpec);
        job.setCreatedAt(entity.getCreatedAt());
        job.setCreatedBy(entity.getCreatedBy());
        job.setEntId(entity.getEntId());
        job.setUserId(entity.getUserId());
        job.setStatus(entity.getStatus());
        job.setDeliveryConfig(toDeliveryConfig(entity));
        return job;
    }

    private DeliveryConfig toDeliveryConfig(ScheduledReportEntity entity) {
        if (entity.getDeliveryWay() == null
                && entity.getEmailTo() == null
                && entity.getEmailSubject() == null
                && entity.getEmailBody() == null) {
            return null;
        }
        EmailConfig emailConfig = null;
        if (entity.getEmailTo() != null || entity.getEmailSubject() != null || entity.getEmailBody() != null) {
            emailConfig = new EmailConfig(
                    entity.getEmailTo(),
                    entity.getEmailSubject(),
                    entity.getEmailBody()
            );
        }
        return new DeliveryConfig(entity.getDeliveryWay(), null, emailConfig);
    }
}
