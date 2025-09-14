package com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils.strategy;

import com.unicauca.edu.co.auxiliary_book.application.dto.DiaryBookDTO;
import com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils.AccountingInfoProcessor;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.AuxiliaryBookCriteria;
import com.unicauca.edu.co.auxiliary_book.domain.models.external.accountingInfo.AccountingInfo;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@NoArgsConstructor
public class DiaryStrategy implements IProcessStrategy {
    @Override
    public List<?> process(AuxiliaryBookCriteria criteria, List<AccountingInfo> data, AccountingInfoProcessor accountingInfoProcessor) {
        return accountingInfoProcessor.calculateBalancesByCriteriaGroup(
                        data,
                        criteria,
                        (groupKey, groupItems) -> {
                            AccountingInfo reference = groupItems.get(0);

                            // Totales de débitos y créditos
                            BigDecimal totalDebit = BigDecimal.ZERO;
                            BigDecimal totalCredit = BigDecimal.ZERO;

                            for (AccountingInfo item : groupItems) {
                                String nature = item.getAccount().getNature();
                                BigDecimal debit = BigDecimal.valueOf(item.getAccountingMovement().getDebit());
                                BigDecimal credit = BigDecimal.valueOf(item.getAccountingMovement().getCredit());

                                if ("Debito".equalsIgnoreCase(nature)) {
                                    // Naturaleza Débito → se registran normal
                                    totalDebit = totalDebit.add(debit);
                                    totalCredit = totalCredit.add(credit);
                                } else if ("Credito".equalsIgnoreCase(nature)) {
                                    // Naturaleza Crédito → también se registran normal
                                    totalDebit = totalDebit.add(debit);
                                    totalCredit = totalCredit.add(credit);
                                }
                            }

                            return new DiaryBookDTO(
                                    reference.getDate(),
                                    groupKey,
                                    reference.getAccount().getName(),
                                    reference.getVoucher().getType(),
                                    reference.getVoucher().getNumber(),
                                    totalDebit,
                                    totalCredit
                            );
                        }
                )
                .stream()
                .sorted(Comparator.comparing(DiaryBookDTO::getAccountCode))
                .sorted(Comparator.comparing(DiaryBookDTO::getDate))
                .toList();
    }

}
