package com.unicauca.edu.co.auxiliary_book.domain.models.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountingInfo {
    private LocalDate date;
    private String voucherNumber;
    private String voucherType;
    private String accountCode;
    private String accountName;
    private Long thirdPartyId;
    private String description;
    private Double debit;
    private Double credit;
    private Double balance;
}
