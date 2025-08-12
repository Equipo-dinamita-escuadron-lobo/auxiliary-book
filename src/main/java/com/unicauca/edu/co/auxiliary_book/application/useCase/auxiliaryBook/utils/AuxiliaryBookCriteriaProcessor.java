package com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils;

import com.unicauca.edu.co.auxiliary_book.application.dto.InventoryAndBalancesBookDTO;
import com.unicauca.edu.co.auxiliary_book.application.ports.out.IAccountingInfoQueryPort;
import com.unicauca.edu.co.auxiliary_book.domain.models.external.accountingInfo.AccountingInfo;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.AuxiliaryBookCriteria;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.CriteriaRange;
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
        String accCode = info.getAccount().getCode().toString();
        var range = criteria.getCriteriaRange();

        switch (criteria.getCriteriaType()) {
            case NUMBER_CLASS -> {
                long classDigit = Integer.parseInt(accCode.substring(0, 1));
                return isInRange(classDigit, range);
            }
            case GROUP -> {
                long groupDigits = Integer.parseInt(accCode.substring(0, 2));
                return isInRange(groupDigits, range);
            }
            case ACCOUNT -> {
                long accDigits = Integer.parseInt(accCode.substring(0, 4));
                return isInRange(accDigits, range);
            }
            case SUB_ACCOUNT -> {
                long subAcc = Integer.parseInt(accCode.substring(0, 6));
                return isInRange(subAcc, range);
            }
            case AUXILIARY_ACCOUNT -> {
                long aux = Integer.parseInt(accCode.length() >= 8 ? accCode.substring(0, 8) : accCode);
                return isInRange(aux, range);
            }
            default -> throw new IllegalArgumentException("Criterio no reconocido");
        }
    }

    private List<AccountingInfo> filterAccountingInfoByCriteria(AuxiliaryBookCriteria criteria, List<AccountingInfo> all) {
        if(criteria.hasRange()){
            return all.parallelStream()
                    .filter(info -> matchesCriteria(info, criteria))
                    .toList();
        }
        return all;
    }

    private List<InventoryAndBalancesBookDTO> processInventoryAndBalances(AuxiliaryBookCriteria criteria, List<AccountingInfo> data) {
        if (!criteria.hasRange()) {

            return accountingInfoProcessor.calculateBalancesByCriteriaGroup(
                    data,
                    criteria,
                    (groupKey, groupItems) -> {
                        AccountingInfo firstItem = groupItems.get(0);

                        double totalBalance = groupItems.stream()
                                .mapToDouble(item -> {
                                    String nature = item.getAccount().getNature();
                                    double debit = item.getAccountingMovement().getDebit();
                                    double credit = item.getAccountingMovement().getCredit();

                                    if ("Debito".equalsIgnoreCase(nature)) {
                                        return debit;
                                    } else if ("Credito".equalsIgnoreCase(nature)) {
                                        return -credit;
                                    }
                                    return 0;
                                })
                                .sum();

                        return new InventoryAndBalancesBookDTO(
                                groupKey,
                                firstItem.getAccount().getName(),
                                firstItem.getAccountingMovement().getDescription(),
                                totalBalance
                        );
                    }
            );
        }

        return this.accountingInfoProcessor.calculateBalancesByCriteriaGroup(
                data,
                criteria,
                (groupKey, groupItems) -> {
                    AccountingInfo firstItem = groupItems.get(0);

                    double totalBalance = groupItems.stream()
                            .mapToDouble(item -> {
                                String nature = item.getAccount().getNature();
                                double debit = item.getAccountingMovement().getDebit();
                                double credit = item.getAccountingMovement().getCredit();

                                if ("Debito".equalsIgnoreCase(nature)) {
                                    return debit;
                                } else if ("Credito".equalsIgnoreCase(nature)) {
                                    return -credit;
                                }
                                return 0;
                            })
                            .sum();

                    return new InventoryAndBalancesBookDTO(
                            groupKey,
                            firstItem.getAccount().getName(),
                            firstItem.getAccountingMovement().getDescription(),
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

    private boolean isInRange(Long value, CriteriaRange range) {
        return (range.getFromRange() == null || value.compareTo(range.getFromRange()) >= 0)
                && (range.getToRange() == null || value.compareTo(range.getToRange()) <= 0);
    }

}
