package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.adapter.ScheduledReport;

import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EJobStatus;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduling.DeliveryConfig;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduling.EmailConfig;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduling.ScheduleSpec;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduling.ScheduledAuxiliaryBookJob;
import com.unicauca.edu.co.auxiliary_book.domain.ports.scheduledReport.IScheduledReportCommandRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookCriteriaEntity;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.scheduledReport.ScheduledReportEntity;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.mapper.IAuxiliaryBookCommandEntityMapper;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.mapper.IAuxiliaryBookCriteriaCommandEntityMapper;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.repository.scheduledReport.IScheduledReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ScheduledReportCommandRepositoryAdapter implements IScheduledReportCommandRepositoryPort {

    private final IScheduledReportRepository scheduledReportRepository;
    private final IAuxiliaryBookCriteriaCommandEntityMapper criteriaCommandEntityMapper;
    private final IAuxiliaryBookCommandEntityMapper auxiliaryBookCommandEntityMapper;

    @Override
    public ScheduledAuxiliaryBookJob save(ScheduledAuxiliaryBookJob job) {
        ScheduledReportEntity entity = toEntity(job);
        ScheduledReportEntity saved = scheduledReportRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public void updateStatus(Long jobId, EJobStatus status) {
        if (jobId == null || status == null) {
            return;
        }
        Optional<ScheduledReportEntity> entityOptional = scheduledReportRepository.findById(jobId);
        if (entityOptional.isEmpty()) {
            return;
        }
        ScheduledReportEntity entity = entityOptional.get();
        entity.setStatus(status);
        scheduledReportRepository.save(entity);
    }

    private ScheduledReportEntity toEntity(ScheduledAuxiliaryBookJob job) {
        ScheduledReportEntity entity = new ScheduledReportEntity();
        entity.setId(job.getJobId());
        entity.setPublicId(job.getPublicId());
        entity.setBookType(job.getBookType());
        entity.setFrequency(job.getScheduleSpec().getFrequency());
        entity.setStartAt(job.getScheduleSpec().getStartAt());
        entity.setEndAt(job.getScheduleSpec().getEndAt() != null ? job.getScheduleSpec().getEndAt().orElse(null) : null);
        entity.setNextRunAt(job.getScheduleSpec().getNextRunAt());
        entity.setCreatedAt(job.getCreatedAt());
        entity.setCreatedBy(job.getCreatedBy());
        entity.setEntId(job.getEntId());
        entity.setUserId(job.getUserId());
        entity.setStatus(job.getStatus());

        DeliveryConfig deliveryConfig = job.getDeliveryConfig();
        if (deliveryConfig != null) {
            entity.setDeliveryWay(deliveryConfig.getDeliveryWay());
            EmailConfig emailConfig = deliveryConfig.getEmailConfig();
            if (emailConfig != null) {
                entity.setEmailTo(emailConfig.getTo());
                entity.setEmailSubject(emailConfig.getSubjectTemplate());
                entity.setEmailBody(emailConfig.getBodyTemplate());
            }
        }

        AuxiliaryBookCriteriaEntity criteriaEntity = criteriaCommandEntityMapper.toCriteriaEntity(job.getCriteria());
        entity.setCriteria(criteriaEntity);
        return entity;
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
