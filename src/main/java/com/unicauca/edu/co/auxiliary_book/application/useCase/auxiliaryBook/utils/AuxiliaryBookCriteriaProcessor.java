package com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils;

import com.unicauca.edu.co.auxiliary_book.application.dto.InventoryAndBalancesBookDTO;
import com.unicauca.edu.co.auxiliary_book.application.ports.out.IAccountingInfoQueryPort;
import com.unicauca.edu.co.auxiliary_book.domain.models.external.AccountingInfo;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.AuxiliaryBookCriteria;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.CriteriaRange;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuxiliaryBookCriteriaProcessor {

    private final AccountingInfoProcessor accountingInfoProcessor;

    public AuxiliaryBookCriteriaProcessor(){
        this.accountingInfoProcessor = new AccountingInfoProcessor();
    }

    public List<?> process(IAccountingInfoQueryPort queryPort, AuxiliaryBook book) {
        AuxiliaryBookCriteria criteria = book.getCriteria();

        criteria.validate();

        List<AccountingInfo> allAccountData = queryPort.getAllAccountInfo();
        List<AccountingInfo> filteredAccountData = this.filterAccountingInfoByCriteria(criteria,allAccountData);

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

    private boolean matchesCriteria(AccountingInfo info, AuxiliaryBookCriteria criteria) {
        String accCode = info.getAccountCode();

        switch (criteria.getCriteriaType()) {
            case NUMBER_CLASS -> {
                var range = criteria.getNumberClassRange();
                int classDigit = Integer.parseInt(accCode.substring(0, 1));
                return isInRange(classDigit, range);
            }
            case GROUP -> {
                var range = criteria.getGroupRange();
                int groupDigits = Integer.parseInt(accCode.substring(0, 2));
                return isInRange(groupDigits, range);
            }
            case ACCOUNT -> {
                var range = criteria.getAccountRange();
                long accDigits = Integer.parseInt(accCode.substring(0, 4));
                return isInRange(accDigits, range);
            }
            case SUB_ACCOUNT -> {
                var range = criteria.getSubAccountRange();
                long subAcc = Integer.parseInt(accCode.substring(0, 6));
                return isInRange(subAcc, range);
            }
            case AUXILIARY_ACCOUNT -> {
                var range = criteria.getAuxiliaryAccountRange();
                long aux = Integer.parseInt(accCode.length() >= 8 ? accCode.substring(0, 8) : accCode);
                return isInRange(aux, range);
            }
            default -> throw new IllegalArgumentException("Criterio no reconocido");
        }
    }

    private List<AccountingInfo> filterAccountingInfoByCriteria(AuxiliaryBookCriteria criteria, List<AccountingInfo> all) {
        
        return all.parallelStream()
                .filter(info -> matchesCriteria(info, criteria))
                .toList();
    }

    private List<InventoryAndBalancesBookDTO> processInventoryAndBalances(AuxiliaryBookCriteria criteria, List<AccountingInfo> filteredAccountData) {
        return this.accountingInfoProcessor.calculateBalancesByCriteriaGroup(
          filteredAccountData,
          criteria,
                (groupKey, groupItems) -> {
                    AccountingInfo firstItem = groupItems.get(0);
                    double totalBalance = groupItems.stream()
                            .mapToDouble(AccountingInfo::getBalance)
                            .sum();
                    return new InventoryAndBalancesBookDTO(
                            groupKey,
                            firstItem.getAccountName(),
                            firstItem.getDescription(),
                            totalBalance
                    );
                }
        );
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

    private <T extends Comparable<T>> boolean isInRange(T value, CriteriaRange<T> range) {
        return (range.getFrom() == null || value.compareTo(range.getFrom()) >= 0)
                && (range.getTo() == null || value.compareTo(range.getTo()) <= 0);
    }

}
