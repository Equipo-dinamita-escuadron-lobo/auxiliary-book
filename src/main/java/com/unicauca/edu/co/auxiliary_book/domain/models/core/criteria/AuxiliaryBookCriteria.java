package com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria;

import com.unicauca.edu.co.auxiliary_book.domain.models.enums.ECriteriaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuxiliaryBookCriteria {
    private Long id;

    private ECriteriaType criteriaType;

    private CriteriaRange criteriaRange;

    private String costCenterId;
    private String thirdPartyId;

    private LocalDate startDate;
    private LocalDate endDate;

    public boolean hasRange() {
        return criteriaRange != null;
    }
}
