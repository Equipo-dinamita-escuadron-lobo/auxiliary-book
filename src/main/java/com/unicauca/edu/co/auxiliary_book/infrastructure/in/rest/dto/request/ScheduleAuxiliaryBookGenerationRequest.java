package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.request;

import java.time.ZonedDateTime;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.export.AuxiliaryBookTemplate;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookFormat;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EDeliveryWay;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ScheduleAuxiliaryBookGenerationRequest(
        @NotNull @Future ZonedDateTime executeAt,
        @NotNull EDeliveryWay deliveryWay,
        @Email String emailTo,
        String emailSubject,
        String emailBody,
        @NotNull EAuxiliaryBookFormat format,
        @NotBlank String entName,
        @Valid @NotNull GenerateAuxiliaryBookRequest auxiliaryBook,
        @Valid @NotNull AuxiliaryBookTemplate template
) {
}
