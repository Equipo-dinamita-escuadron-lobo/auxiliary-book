package com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils.strategy;

import com.unicauca.edu.co.auxiliary_book.application.dto.auxiliaryBook.AccountDTO;
import com.unicauca.edu.co.auxiliary_book.application.dto.auxiliaryBook.DiaryBookDTO;
import com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils.AccountingInfoProcessor;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.AuxiliaryBookCriteria;
import com.unicauca.edu.co.auxiliary_book.domain.models.external.accountingInfo.AccountingInfo;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
public class DiaryStrategy implements IProcessStrategy {
    @Override
    public List<?> process(AuxiliaryBookCriteria criteria, List<AccountingInfo> data, AccountingInfoProcessor accountingInfoProcessor) {
        return accountingInfoProcessor.calculateBalancesByCriteriaGroup(
                        data,
                        criteria,
                        (groupKey, groupItems) -> {
                            AccountingInfo reference = groupItems.get(0);

                            AccountDTO accountDTO = new AccountDTO(
                                    reference.getAccount().getNature(),
                                    Long.parseLong(groupKey), // groupKey es el accountCode
                                    reference.getAccount().getName()
                            );

                            BigDecimal totalDebit = groupItems.stream()
                                    .map(item -> item.getAccountingMovement().getDebit())
                                    .filter(Objects::nonNull)
                                    .map(BigDecimal::valueOf)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                            BigDecimal totalCredit = groupItems.stream()
                                    .map(item -> item.getAccountingMovement().getCredit())
                                    .filter(Objects::nonNull)
                                    .map(BigDecimal::valueOf)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                            return new DiaryBookDTO(
                                    reference.getDate(),
                                    accountDTO, // Se pasa el objeto AccountDTO completo
                                    reference.getVoucher().getType(), // voucherName
                                    String.valueOf(reference.getVoucher().getNumber()), // voucherNumber as String
                                    totalDebit,
                                    totalCredit
                            );
                        }
                )
                .stream()
                // --- 3. Ordenamiento corregido ---
                // Se ordena por la llave primaria (Fecha) y luego por la secundaria (Código de cuenta).
                .sorted(Comparator.comparing(DiaryBookDTO::getDate)
                        .thenComparing(dto -> dto.getAccount().getAccountCode()))
                .toList();
    }

}
