package com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils;

import com.unicauca.edu.co.auxiliary_book.application.dto.InventoryAndBalancesBookDTO;
import com.unicauca.edu.co.auxiliary_book.application.ports.out.IAccountingInfoClient;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.AuxiliaryBookCriteria;
import com.unicauca.edu.co.auxiliary_book.domain.models.external.accountingInfo.AccountingInfo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
public class AuxiliaryBookProcessor {
    private final AccountingInfoProcessor accountingInfoProcessor;
    private final AuxiliaryBookCriteriaProcessor auxiliaryBookCriteriaProcessor;

    public AuxiliaryBookProcessor(){
        this.accountingInfoProcessor = new AccountingInfoProcessor();
        this.auxiliaryBookCriteriaProcessor = new AuxiliaryBookCriteriaProcessor();
    }

    public List<?> proccessAuxiliaryBookData(IAccountingInfoClient accountingInfoQueryPort, AuxiliaryBook book){

        AuxiliaryBookCriteria criteria = book.getCriteria();
        List<AccountingInfo> filteredAccountData = this.auxiliaryBookCriteriaProcessor.processAccountingInfo(accountingInfoQueryPort, book);

        return switch (book.getType()) {
            case INVENTORY_AND_BALANCES -> processInventoryAndBalances(criteria, filteredAccountData);
            case DIARY -> processDiary(criteria, filteredAccountData);
            case MAYOR_AND_BALANCES -> processMayor(criteria, filteredAccountData);
            case ACCOUNT -> processAccount(criteria, filteredAccountData);
            case THIRD_PARTY -> processThirdParty(criteria, filteredAccountData);
            case TRIAL_BALANCE -> processTrialBalance(criteria, filteredAccountData);
            case ACCOUNTING_MOVEMENT -> processAccountingMovement(criteria, filteredAccountData);
        };
    }

    private List<InventoryAndBalancesBookDTO> processInventoryAndBalances(
            AuxiliaryBookCriteria criteria,
            List<AccountingInfo> data
    ) {
        return accountingInfoProcessor.calculateBalancesByCriteriaGroup(
                        data,
                        criteria,
                        (groupKey, groupItems) -> {
                            // Tomamos el primer item como referencia para info repetida
                            AccountingInfo reference = groupItems.get(0);

                            // Reducimos en una sola pasada los saldos
                            BigDecimal totalBalance = groupItems.stream()
                                    .map(item -> {
                                        String nature = item.getAccount().getNature();
                                        double debit = item.getAccountingMovement().getDebit();
                                        double credit = item.getAccountingMovement().getCredit();

                                        return "debito".equalsIgnoreCase(nature) ? BigDecimal.valueOf(debit) :
                                                "credito".equalsIgnoreCase(nature) ? BigDecimal.valueOf(-credit) : BigDecimal.ZERO;
                                    }).reduce(BigDecimal.ZERO, BigDecimal::add);

                            return new InventoryAndBalancesBookDTO(
                                    groupKey,
                                    reference.getAccount().getName(),
                                    reference.getAccountingMovement().getDescription(),
                                    totalBalance
                            );
                        }
                )
                // Ordenamos el resultado final por accountCode
                .stream()
                .sorted(Comparator.comparing(InventoryAndBalancesBookDTO::getAccountCode))
                .toList();
    }



    private List<AccountingInfo> processDiary(AuxiliaryBookCriteria criteria, List<AccountingInfo> all) {

        // Lógica de procesado segun criterios específicos
        //TO DO
        return null;
    }

    private List<AccountingInfo> processMayor(AuxiliaryBookCriteria criteria, List<AccountingInfo> all) {

        // Lógica de procesado segun criterios específicos
        //TO DO
        return null;
    }

    private List<AccountingInfo> processAccount(AuxiliaryBookCriteria criteria, List<AccountingInfo> all) {

        // Lógica de procesado segun criterios específicos
        //TO DO
        return null;
    }

    private List<AccountingInfo> processThirdParty(AuxiliaryBookCriteria criteria, List<AccountingInfo> all) {

        // Lógica de procesado segun criterios específicos
        //TO DO
        return null;
    }

    private List<AccountingInfo> processTrialBalance(AuxiliaryBookCriteria criteria, List<AccountingInfo> all) {

        // Lógica de procesado segun criterios específicos
        //TO DO
        return null;
    }

    private List<AccountingInfo> processAccountingMovement(AuxiliaryBookCriteria criteria, List<AccountingInfo> all) {

        // Lógica de procesado segun criterios específicos
        //TO DO
        return null;
    }
}
