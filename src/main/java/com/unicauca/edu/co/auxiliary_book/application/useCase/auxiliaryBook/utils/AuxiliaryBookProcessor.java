package com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils;

import java.util.List;

import org.springframework.stereotype.Service;

import com.unicauca.edu.co.auxiliary_book.application.ports.out.IAccountingInfoClient;
import com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils.strategy.AccountStrategy;
import com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils.strategy.AccountingMovementStrategy;
import com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils.strategy.DiaryStrategy;
import com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils.strategy.InventoryAndBalancesStrategy;
import com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils.strategy.MajorAndBalancesStrategy;
import com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils.strategy.ProcessContext;
import com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils.strategy.ThirdPartyStrategy;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.AuxiliaryBookCriteria;
import com.unicauca.edu.co.auxiliary_book.domain.models.external.accountingInfo.AccountingInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuxiliaryBookProcessor {

    private final AccountingInfoProcessor accountingInfoProcessor;
    private final AuxiliaryBookCriteriaProcessor auxiliaryBookCriteriaProcessor;
    private final ProcessContext processContext;


    public List<?> processAuxiliaryBookData(IAccountingInfoClient accountingInfoQueryPort, AuxiliaryBook book){
        AuxiliaryBookCriteria criteria = book.getCriteria();
        List<AccountingInfo> filteredAccountData = this.auxiliaryBookCriteriaProcessor.processAccountingInfo(accountingInfoQueryPort, book);

        switch (book.getType()) {
            case INVENTORY_AND_BALANCES -> this.processContext.setStrategy(new InventoryAndBalancesStrategy());
            case DIARY -> this.processContext.setStrategy(new DiaryStrategy());
            case MAJOR_AND_BALANCES -> this.processContext.setStrategy(new MajorAndBalancesStrategy());
            case ACCOUNT -> this.processContext.setStrategy(new AccountStrategy());
            case THIRD_PARTY -> this.processContext.setStrategy(new ThirdPartyStrategy());
            case ACCOUNTING_MOVEMENT -> this.processContext.setStrategy(new AccountingMovementStrategy());
        }

        return this.processContext.executeStrategyProcess(criteria, filteredAccountData, this.accountingInfoProcessor);
    }
}
