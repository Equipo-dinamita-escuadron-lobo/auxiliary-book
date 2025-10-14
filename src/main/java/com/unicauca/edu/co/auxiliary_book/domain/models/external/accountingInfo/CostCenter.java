package com.unicauca.edu.co.auxiliary_book.domain.models.external.accountingInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @brief Domain model representing a Cost Center.
 *
 */
@Data
@AllArgsConstructor @NoArgsConstructor
public class CostCenter {
    private String code;
    private String name;
}
