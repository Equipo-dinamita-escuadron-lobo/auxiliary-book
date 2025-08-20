package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.request;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.AuxiliaryBookCriteria;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateAuxiliaryBookRequest {
    @NotEmpty(message = "Auxiliary Enterprise ID cannot be Empty")
    private String entId;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotEmpty(message = "Auxiliary Book Type cannot be Empty")
    private EAuxiliaryBookType type;

    @NotNull(message = "Auxiliary Book Criteria cannot be null")
    private AuxiliaryBookCriteria criteria;
}
