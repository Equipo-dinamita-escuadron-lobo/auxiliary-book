package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.mapper;

import java.util.Optional;

import org.mapstruct.Mapper;

import com.unicauca.edu.co.auxiliary_book.domain.models.scheduling.DeliveryConfig;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduling.ScheduleSpec;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduling.ScheduledAuxiliaryBookJob;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.request.CreateScheduledReportRequest;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.request.UpdateScheduledReportRequest;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.response.scheduledReport.ScheduledReportListItemResponse;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.response.scheduledReport.ScheduledReportResponse;

/**
 * @brief Mapper REST entre las solicitudes/respuestas y el dominio de reportes programados.
 *
 * Provee métodos por defecto para convertir entre los DTOs de la capa
 * REST ({@link CreateScheduledReportRequest}, {@link UpdateScheduledReportRequest},
 * {@link ScheduledReportResponse} y {@link ScheduledReportListItemResponse}) y la
 * entidad de dominio {@link ScheduledAuxiliaryBookJob}, armando las
 * estructuras internas {@link ScheduleSpec} y {@link DeliveryConfig}.
 */
@Mapper(componentModel = "spring")
public interface IScheduledReportRestMapper {

    default ScheduledAuxiliaryBookJob toDomain(CreateScheduledReportRequest request) {
        if (request == null) {
            return null;
        }
        ScheduledAuxiliaryBookJob job = new ScheduledAuxiliaryBookJob();
        job.setEntId(request.getEntId());
        job.setUserId(request.getUserId());
        job.setBookType(request.getBookType());
        job.setCriteria(request.getCriteria());
        job.setCreatedBy(request.getCreatedBy());
        job.setScheduleSpec(new ScheduleSpec(
                request.getFrequency(),
                request.getStartAt(),
                Optional.ofNullable(request.getEndAt()),
                request.getStartAt()
        ));
        job.setDeliveryConfig(new DeliveryConfig(
                request.getDeliveryWay(),
                request.getFormat(),
                request.getEmailConfig()
        ));
        return job;
    }

    default ScheduledAuxiliaryBookJob toDomain(UpdateScheduledReportRequest request) {
        if (request == null) {
            return null;
        }
        ScheduledAuxiliaryBookJob job = new ScheduledAuxiliaryBookJob();
        job.setEntId(request.getEntId());
        job.setUserId(request.getUserId());
        job.setBookType(request.getBookType());
        job.setCriteria(request.getCriteria());
        job.setCreatedBy(request.getCreatedBy());
        job.setScheduleSpec(new ScheduleSpec(
                request.getFrequency(),
                request.getStartAt(),
                Optional.ofNullable(request.getEndAt()),
                request.getStartAt()
        ));
        job.setDeliveryConfig(new DeliveryConfig(
                request.getDeliveryWay(),
                request.getFormat(),
                request.getEmailConfig()
        ));
        return job;
    }

    default ScheduledReportResponse toResponse(ScheduledAuxiliaryBookJob job) {
        if (job == null) {
            return null;
        }
        ScheduledReportResponse response = new ScheduledReportResponse();
        response.setPublicId(job.getPublicId());
        response.setBookType(job.getBookType());
        response.setCriteria(job.getCriteria());
        response.setFrequency(job.getScheduleSpec() != null ? job.getScheduleSpec().getFrequency() : null);
        response.setStartAt(job.getScheduleSpec() != null ? job.getScheduleSpec().getStartAt() : null);
        response.setEndAt(job.getScheduleSpec() != null && job.getScheduleSpec().getEndAt() != null
                ? job.getScheduleSpec().getEndAt().orElse(null)
                : null);
        response.setNextRunAt(job.getScheduleSpec() != null ? job.getScheduleSpec().getNextRunAt() : null);
        response.setStatus(job.getStatus());
        response.setEntId(job.getEntId());
        response.setUserId(job.getUserId());
        response.setCreatedBy(job.getCreatedBy());
        response.setCreatedAt(job.getCreatedAt());
        response.setDeliveryWay(job.getDeliveryConfig() != null ? job.getDeliveryConfig().getDeliveryWay() : null);
        response.setEmailConfig(job.getDeliveryConfig() != null ? job.getDeliveryConfig().getEmailConfig() : null);
        return response;
    }

    default ScheduledReportListItemResponse toListItemResponse(ScheduledAuxiliaryBookJob job) {
        if (job == null) {
            return null;
        }
        ScheduledReportListItemResponse response = new ScheduledReportListItemResponse();
        response.setPublicId(job.getPublicId());
        response.setBookType(job.getBookType());
        response.setFrequency(job.getScheduleSpec() != null ? job.getScheduleSpec().getFrequency() : null);
        response.setNextRunAt(job.getScheduleSpec() != null ? job.getScheduleSpec().getNextRunAt() : null);
        response.setStatus(job.getStatus());
        return response;
    }
}
