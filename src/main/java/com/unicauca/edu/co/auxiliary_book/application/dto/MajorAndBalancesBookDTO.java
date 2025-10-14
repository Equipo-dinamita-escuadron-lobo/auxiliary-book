package com.unicauca.edu.co.auxiliary_book.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MajorAndBalancesBookDTO {
    private String accountCode;
    private String accountDescription;
    private BigDecimal initialBalance;
    private BigDecimal debitMovement;
    private BigDecimal creditMovement;
    private BigDecimal finalBalance;
}
