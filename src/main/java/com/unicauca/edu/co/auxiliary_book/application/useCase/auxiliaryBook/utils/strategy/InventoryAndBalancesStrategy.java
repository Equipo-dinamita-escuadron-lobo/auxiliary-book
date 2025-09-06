package com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils.strategy;

import com.unicauca.edu.co.auxiliary_book.application.dto.InventoryAndBalancesBookDTO;
import com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils.AccountingInfoProcessor;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.AuxiliaryBookCriteria;
import com.unicauca.edu.co.auxiliary_book.domain.models.external.accountingInfo.AccountingInfo;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@NoArgsConstructor
public class InventoryAndBalancesStrategy implements ProcessStrategy {

    @Override
    public List<?> process(AuxiliaryBookCriteria criteria, List<AccountingInfo> data, AccountingInfoProcessor accountingInfoProcessor) {
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
}
