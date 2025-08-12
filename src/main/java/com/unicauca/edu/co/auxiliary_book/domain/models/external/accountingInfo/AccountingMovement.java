package com.unicauca.edu.co.auxiliary_book.domain.models.external.accountingInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class AccountingMovement {
    private String description;
    private Double debit;
    private Double credit;
}
