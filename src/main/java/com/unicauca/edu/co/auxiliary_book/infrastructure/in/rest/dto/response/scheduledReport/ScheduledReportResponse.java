package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.response.scheduledReport;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.AuxiliaryBookCriteria;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookType;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EDeliveryWay;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EFrequency;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EJobStatus;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduling.EmailConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * @brief DTO de respuesta con el detalle completo de un reporte programado.
 *
 * Expone la información completa de un reporte programado: identidad,
 * tipo de libro, criterios, especificación de frecuencia y fechas,
 * estado del job, metadatos de auditoría y configuración de entrega.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduledReportResponse {
    private String publicId;
    private EAuxiliaryBookType bookType;
    private AuxiliaryBookCriteria criteria;
    private EFrequency frequency;
    private Instant startAt;
    private Instant endAt;
    private Instant nextRunAt;
    private EJobStatus status;
    private String entId;
    private Long userId;
    private String createdBy;
    private LocalDateTime createdAt;
    private EDeliveryWay deliveryWay;
    private EmailConfig emailConfig;
}
