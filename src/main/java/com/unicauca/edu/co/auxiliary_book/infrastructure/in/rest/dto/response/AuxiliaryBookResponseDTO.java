package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor @NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuxiliaryBookResponseDTO {
    private AuxiliaryBook auxiliaryBook;
    private List<?> accountingData;
}
