package com.unicauca.edu.co.auxiliary_book.application.dto.auxiliaryBook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountingMovementBookDTO {
    private String voucherType;
    private Date date;
    private String State;
    private String thirdPartyId;
    private String thirdPartyName;
    private AccountDTO account;
    private BigDecimal initialBalance;
    private BigDecimal debitMovement;
    private BigDecimal creditMovement;
    private BigDecimal netMovement;

    public Long getAccountCode() {
        return account != null ? account.getAccountCode() : null;
    }

    public String getAccountDescription() {
        return account != null ? account.getAccountDescription() : null;
    }
}
