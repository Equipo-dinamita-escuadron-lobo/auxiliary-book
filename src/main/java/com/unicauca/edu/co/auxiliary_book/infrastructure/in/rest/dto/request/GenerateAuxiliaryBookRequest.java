package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.request;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBookTemplate;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.AuxiliaryBookCriteria;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookFormat;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateAuxiliaryBookRequest {
    @NotEmpty(message = "Auxiliary Book Type cannot be Empty")
    private EAuxiliaryBookType type;
    @NotEmpty(message = "Auxiliary Enterprise ID cannot be Empty")
    private String entId;
    @NotNull(message = "User ID cannot be null")
    private Long userId;
    @NotNull(message = "Auxiliary Book Criteria cannot be null")
    private AuxiliaryBookCriteria criteria;
}
