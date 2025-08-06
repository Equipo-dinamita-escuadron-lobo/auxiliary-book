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

    private CriteriaRange<Integer> numberClassRange;
    private CriteriaRange<Integer> groupRange;
    private CriteriaRange<Long> accountRange;
    private CriteriaRange<Long> subAccountRange;
    private CriteriaRange<Long> auxiliaryAccountRange;

    private Long costCenterId;
    private Long thirdPartyId;
    private LocalDate startDate;
    private LocalDate endDate;

    public void validate() {
        int count = 0;
        if (numberClassRange != null) count++;
        if (groupRange != null) count++;
        if (accountRange != null) count++;
        if (subAccountRange != null) count++;
        if (auxiliaryAccountRange != null) count++;

        if (count > 1) {
            throw new IllegalArgumentException("Solo se debe especificar un único tipo de criterio de generacion.");
        }

        if (criteriaType == null) {
            throw new IllegalArgumentException("Debe especificar el tipo de criterio para la generacion del libro auxiliar.");
        }

        // Validación cruzada
        switch (criteriaType) {
            case NUMBER_CLASS -> {
                if (numberClassRange == null) throw new IllegalArgumentException("Debe proporcionar rango para la Clase");
            }
            case GROUP -> {
                if (groupRange == null) throw new IllegalArgumentException("Debe proporcionar rango para GROUP");
            }
            case ACCOUNT -> {
                if (accountRange == null) throw new IllegalArgumentException("Debe proporcionar rango para ACCOUNT");
            }
            case SUB_ACCOUNT -> {
                if (subAccountRange == null) throw new IllegalArgumentException("Debe proporcionar rango para SUB_ACCOUNT");
            }
            case AUXILIARY_ACCOUNT -> {
                if (auxiliaryAccountRange == null) throw new IllegalArgumentException("Debe proporcionar rango para AUXILIARY_ACCOUNT");
            }
        }
    }
}
