package com.unicauca.edu.co.auxiliary_book.domain.models.external.accountingInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @brief Domain model representing an Accounting Movement.
 *
 */
@Data
@AllArgsConstructor @NoArgsConstructor
public class AccountingMovement {
    private String description;
    private Double debit;
    private Double credit;
}