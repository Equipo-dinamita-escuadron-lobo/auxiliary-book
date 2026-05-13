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

/**
 * @brief Adaptador de lectura para los trabajos de reportes programados.
 *
 * Implementa {@link IScheduledReportQueryRepositoryPort} consultando la
 * base de datos a través de {@link IScheduledReportRepository} y mapeando
 * las entidades JPA al dominio {@link ScheduledAuxiliaryBookJob},
 * incluyendo los criterios del libro auxiliar y la configuración de
 * entrega asociada.
 */
@Component
@RequiredArgsConstructor
public class ScheduledReportQueryRepositoryAdapter implements IScheduledReportQueryRepositoryPort {

    private final IScheduledReportRepository scheduledReportRepository;
    private final IAuxiliaryBookCommandEntityMapper auxiliaryBookCommandEntityMapper;

    /**
     * @brief Busca un trabajo programado por su ID interno.
     * @param jobId Identificador interno.
     * @return Trabajo programado si existe.
     */
    @Override
    public Optional<ScheduledAuxiliaryBookJob> findById(Long jobId) {
        return scheduledReportRepository.findById(jobId).map(this::toDomain);
    }

    /**
     * @brief Busca un trabajo programado por su identificador público.
     * @param publicId Identificador público (UUID).
     * @return Trabajo programado si existe.
     */
    @Override
    public Optional<ScheduledAuxiliaryBookJob> findByPublicId(String publicId) {
        return scheduledReportRepository.findByPublicId(publicId).map(this::toDomain);
    }

    /**
     * @brief Obtiene todos los trabajos programados de una empresa.
     * @param entId Identificador de la empresa.
     * @return Lista de trabajos programados asociados.
     */
    @Override
    public List<ScheduledAuxiliaryBookJob> findByEntId(String entId) {
        return scheduledReportRepository.findByEntId(entId).stream().map(this::toDomain).toList();
    }

    /**
     * @brief Obtiene los trabajos activos cuya próxima ejecución ya venció.
     * @param now Instante de referencia para comparar {@code nextRunAt}.
     * @param limit Tamaño máximo del lote a retornar (por defecto 50 si no es positivo).
     * @return Lista de trabajos elegibles para ejecución ordenados por {@code nextRunAt} ascendente.
     */
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
        job.setOwnerSub(entity.getOwnerSub());
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
