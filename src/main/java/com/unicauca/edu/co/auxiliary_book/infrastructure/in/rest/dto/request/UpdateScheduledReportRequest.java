package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.request;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.AuxiliaryBookCriteria;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookType;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EDeliveryWay;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EFrequency;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduling.EmailConfig;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * @brief DTO de solicitud para actualizar un reporte programado.
 *
 * Define los campos modificables de un reporte programado existente:
 * empresa, usuario, tipo de libro, criterios, frecuencia, ventana de
 * ejecución, vía de entrega y configuración de correo. Se aplica
 * sobre un reporte identificado por su {@code publicId} en el endpoint.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateScheduledReportRequest {

    @NotEmpty(message = "Enterprise ID cannot be empty")
    private String entId;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Book type cannot be null")
    private EAuxiliaryBookType bookType;

    @NotNull(message = "Criteria cannot be null")
    private AuxiliaryBookCriteria criteria;

    @NotNull(message = "Frequency cannot be null")
    private EFrequency frequency;

    @NotNull(message = "Start date cannot be null")
    private Instant startAt;

    private Instant endAt;

    private String createdBy;

    @NotNull(message = "Delivery way cannot be null")
    private EDeliveryWay deliveryWay;

    private EmailConfig emailConfig;
}
