package com.unicauca.edu.co.auxiliary_book.domain.models.external.accountingInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @brief Domain model representing an Account.
 *
 */
@Data
@AllArgsConstructor @NoArgsConstructor
public class Account {
    private Long code;
    private String nature;
    private String name;
}
