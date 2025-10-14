package com.unicauca.edu.co.auxiliary_book.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThirdPartyBookDTO {
    private LocalDate date;
    private String accountCode;
    private String accountDescription;
    private BigDecimal debitMovement;
    private BigDecimal creditMovement;
    private BigDecimal balanceMovement;
    private String thirdPartyId;
    private String thirdPartyName;
    private String voucherCostCenter;
    private String voucherNumber;
}
