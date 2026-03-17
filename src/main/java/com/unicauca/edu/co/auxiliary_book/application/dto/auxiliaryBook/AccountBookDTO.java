package com.unicauca.edu.co.auxiliary_book.application.dto.auxiliaryBook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountBookDTO {
    private Date date;
    private AccountDTO account;
    private BigDecimal debitMovement;
    private BigDecimal creditMovement;
    private BigDecimal balanceMovement;
    private String thirdPartyId;
    private String thirdPartyName;
    private String voucherCostCenter;
    private String voucherNumber;

    public Long getAccountCode() {
        return account != null ? account.getAccountCode() : null;
    }

    public String getAccountDescription() {
        return account != null ? account.getAccountDescription() : null;
    }
}
