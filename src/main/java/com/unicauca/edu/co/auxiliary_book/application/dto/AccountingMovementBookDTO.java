package com.unicauca.edu.co.auxiliary_book.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountingMovementBookDTO {
    private String voucherType;
    private LocalDate date;
    private String State;
    private String thirdPartyId;
    private String thirdPartyName;
    private String accountCode;
    private String accountDescription;
    private BigDecimal initialBalance;
    private BigDecimal debitMovement;
    private BigDecimal creditMovement;
    private BigDecimal netMovement;
}
