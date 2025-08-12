package com.unicauca.edu.co.auxiliary_book.domain.models.external.accountingInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountingInfo {
    private String entId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate date;

    private Voucher voucher;
    private Account account;
    private Long thirdPartyId;
    private AccountingMovement accountingMovement;
    private CostCenter costCenter;
}
