package com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils.strategy;

import com.unicauca.edu.co.auxiliary_book.application.dto.auxiliaryBook.AccountDTO;
import com.unicauca.edu.co.auxiliary_book.application.dto.auxiliaryBook.MajorAndBalancesBookDTO;
import com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils.AccountingInfoProcessor;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.AuxiliaryBookCriteria;
import com.unicauca.edu.co.auxiliary_book.domain.models.external.accountingInfo.AccountingInfo;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
public class MajorAndBalancesStrategy implements IProcessStrategy {

    @Override
    public List<?> process(
            AuxiliaryBookCriteria criteria,
            List<AccountingInfo> allAccountingData,
            AccountingInfoProcessor accountingInfoProcessor) {

        LocalDate startDate = criteria.getStartDate();
        LocalDate endDate = criteria.getEndDate();

        if (startDate == null) {
            System.err.println("⚠️ No se especificó una fecha de inicio en los criterios del libro mayor.");
            return Collections.emptyList();
        }

        // --- 1️⃣ Filtrar los movimientos por fechas ---
        List<AccountingInfo> previousPeriodData = allAccountingData.stream()
                .filter(info -> {
                    Date date = info.getDate();
                    if (date == null) return false;
                    LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return localDate.isBefore(startDate);
                })
                .toList();

        List<AccountingInfo> currentPeriodData = allAccountingData.stream()
                .filter(info -> {
                    Date date = info.getDate();
                    if (date == null) return false;
                    LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return !localDate.isBefore(startDate) && !localDate.isAfter(endDate);
                })
                .toList();

        // --- 2️⃣ Calcular los saldos iniciales ---
        Map<Long, BigDecimal> initialBalances = previousPeriodData.stream()
                .collect(Collectors.groupingBy(
                        info -> info.getAccount().getCode(),
                        Collectors.collectingAndThen(Collectors.toList(), this::calculateBalanceForAccount)
                ));

        // --- 3️⃣ Agrupar movimientos del periodo actual ---
        Map<Long, List<AccountingInfo>> movementsByAccount = currentPeriodData.stream()
                .collect(Collectors.groupingBy(info -> info.getAccount().getCode()));

        // --- 4️⃣ Consolidar todas las cuentas (saldo previo + movimientos del periodo) ---
        Set<Long> allAccountCodes = new HashSet<>(initialBalances.keySet());
        allAccountCodes.addAll(movementsByAccount.keySet());

        List<MajorAndBalancesBookDTO> result = new ArrayList<>();

        for (Long accountCode : allAccountCodes) {
            List<AccountingInfo> movements = movementsByAccount.getOrDefault(accountCode, Collections.emptyList());
            AccountingInfo reference = !movements.isEmpty()
                    ? movements.get(0)
                    : previousPeriodData.stream()
                    .filter(info -> info.getAccount().getCode().equals(accountCode))
                    .findFirst()
                    .orElse(null);

            if (reference == null) continue; // no hay referencia contable

            String nature = reference.getAccount().getNature();
            String description = reference.getAccount().getName();
            BigDecimal initialBalance = initialBalances.getOrDefault(accountCode, BigDecimal.ZERO);

            BigDecimal totalDebit = movements.stream()
                    .map(info -> info.getAccountingMovement().getDebit())
                    .filter(Objects::nonNull)
                    .map(BigDecimal::valueOf)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalCredit = movements.stream()
                    .map(info -> info.getAccountingMovement().getCredit())
                    .filter(Objects::nonNull)
                    .map(BigDecimal::valueOf)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal finalBalance = calculateFinalBalance(initialBalance, totalDebit, totalCredit, nature);

            AccountDTO accountDTO = new AccountDTO(nature, accountCode, description);

            result.add(new MajorAndBalancesBookDTO(
                    accountDTO,
                    initialBalance.setScale(2, RoundingMode.HALF_UP),
                    totalDebit.setScale(2, RoundingMode.HALF_UP),
                    totalCredit.setScale(2, RoundingMode.HALF_UP),
                    finalBalance.setScale(2, RoundingMode.HALF_UP)
            ));
        }

        // --- 5️⃣ Ordenar por código de cuenta ---
        result.sort(Comparator.comparing(dto -> dto.getAccount().getAccountCode()));
        return result;
    }

    /**
     * Calcula el saldo neto de una cuenta según su naturaleza
     * para el conjunto de movimientos recibido.
     */
    private BigDecimal calculateBalanceForAccount(List<AccountingInfo> movements) {
        if (movements.isEmpty()) return BigDecimal.ZERO;

        AccountingInfo ref = movements.get(0);
        String nature = ref.getAccount().getNature();

        BigDecimal totalDebit = movements.stream()
                .map(info -> info.getAccountingMovement().getDebit())
                .filter(Objects::nonNull)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCredit = movements.stream()
                .map(info -> info.getAccountingMovement().getCredit())
                .filter(Objects::nonNull)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return calculateFinalBalance(BigDecimal.ZERO, totalDebit, totalCredit, nature);
    }

    /**
     * Calcula el saldo final basado en la naturaleza de la cuenta.
     */
    private BigDecimal calculateFinalBalance(BigDecimal initial, BigDecimal debit, BigDecimal credit, String nature) {
        if ("debito".equalsIgnoreCase(nature)) {
            return initial.add(debit).subtract(credit);
        } else if ("credito".equalsIgnoreCase(nature)) {
            return initial.subtract(debit).add(credit);
        }
        return initial.add(debit).subtract(credit); // por defecto naturaleza deudora
    }
}
