package com.unicauca.edu.co.auxiliary_book.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor @NoArgsConstructor
public class DiaryBookDTO {
    private LocalDate date;
    private String accountCode;
    private String accountDescription;
    private String voucherName;
    private String voucherNumber;
    private BigDecimal debit;
    private BigDecimal credit;
}
