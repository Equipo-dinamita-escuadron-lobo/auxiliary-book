package com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils.strategy;

import com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils.AccountingInfoProcessor;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.AuxiliaryBookCriteria;
import com.unicauca.edu.co.auxiliary_book.domain.models.external.accountingInfo.AccountingInfo;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class MajorAndBalancesStrategy implements IProcessStrategy {
    @Override
    public List<?> process(AuxiliaryBookCriteria criteria, List<AccountingInfo> data, AccountingInfoProcessor accountingInfoProcessor) {
        return List.of();
    }
}
