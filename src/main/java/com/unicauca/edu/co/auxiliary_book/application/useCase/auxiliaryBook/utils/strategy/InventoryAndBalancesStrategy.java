package com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils.strategy;

import com.unicauca.edu.co.auxiliary_book.application.dto.auxiliaryBook.AccountDTO;
import com.unicauca.edu.co.auxiliary_book.application.dto.auxiliaryBook.InventoryAndBalancesBookDTO;
import com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils.AccountingInfoProcessor;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.AuxiliaryBookCriteria;
import com.unicauca.edu.co.auxiliary_book.domain.models.external.accountingInfo.AccountingInfo;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
public class InventoryAndBalancesStrategy implements IProcessStrategy {

    @Override
    public List<?> process(AuxiliaryBookCriteria criteria, List<AccountingInfo> data, AccountingInfoProcessor accountingInfoProcessor) {
        return accountingInfoProcessor.calculateBalancesByCriteriaGroup(
                        data,
                        criteria,
                        (groupKey, groupItems) -> {
                            // Tomamos el primer item como referencia para info repetida
                            AccountingInfo reference = groupItems.get(0);

                            // --- 1. Mapeo de Account a AccountDTO ---
                            AccountDTO accountDTO = new AccountDTO(
                                    reference.getAccount().getNature(),
                                    Long.parseLong(groupKey), // groupKey es el accountCode
                                    reference.getAccount().getName()
                            );

                            // --- 2. Lógica de totales robusta ---
                            // Calculamos débitos y créditos totales del periodo (asumiendo saldo inicial 0)
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

                            // --- 3. Calcular el Saldo/Valor Final ---
                            // Aplicamos la regla contable según la naturaleza (asumiendo Saldo Inicial 0)
                            String nature = reference.getAccount().getNature();
                            BigDecimal finalValue;

                            if ("debito".equalsIgnoreCase(nature)) {
                                // Saldo Final = Saldo Inicial (0) + Débitos - Créditos
                                finalValue = totalDebit.subtract(totalCredit);
                            } else if ("credito".equalsIgnoreCase(nature)) {
                                // Saldo Final = Saldo Inicial (0) - Débitos + Créditos
                                finalValue = totalCredit.subtract(totalDebit);
                            } else {
                                // Default a debito si la naturaleza es nula o desconocida
                                finalValue = totalDebit.subtract(totalCredit);
                            }

                            // --- 4. Construir DTO ---
                            // Nota: la descripción se toma del primer movimiento.
                            // Si deseas la descripción de la cuenta, usa accountDTO.getAccountDescription()
                            return new InventoryAndBalancesBookDTO(
                                    accountDTO,
                                    reference.getAccountingMovement().getDescription(),
                                    finalValue
                            );
                        }
                )
                // Ordenamos el resultado final por accountCode, accediendo a través del DTO anidado
                .stream()
                .sorted(Comparator.comparing(dto -> dto.getAccount().getAccountCode()))
                .toList();
    }
}
