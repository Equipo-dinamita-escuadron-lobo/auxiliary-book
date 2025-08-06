package com.unicauca.edu.co.auxiliary_book.application.ports.out;

import com.unicauca.edu.co.auxiliary_book.domain.models.external.AccountingInfo;

import java.util.List;

public interface IAccountingInfoQueryPort {
    List<AccountingInfo> getAllAccountInfo();
}
