package com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils.strategy;

import com.unicauca.edu.co.auxiliary_book.application.dto.auxiliaryBook.AccountDTO;
import com.unicauca.edu.co.auxiliary_book.application.dto.auxiliaryBook.AccountingMovementBookDTO;
import com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils.AccountingInfoProcessor;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.AuxiliaryBookCriteria;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.ECriteriaType;
import com.unicauca.edu.co.auxiliary_book.domain.models.external.accountingInfo.AccountingInfo;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Estrategia para el Libro de Movimientos Contables (Libro de Comprobantes).
 *
 * <p>Este libro presenta todos los movimientos contables del periodo agrupados por
 * comprobante ({@code voucherType} + {@code voucherNumber}), mostrando para cada
 * asiento dentro del comprobante el saldo acumulado de la cuenta hasta ese momento
 * (incluyendo el periodo anterior como saldo inicial).</p>
 *
 * <p>Estructura de cada fila ({@link AccountingMovementBookDTO}):</p>
 * <ul>
 *   <li>{@code voucherType} — tipo de comprobante</li>
 *   <li>{@code date} — fecha del asiento</li>
 *   <li>{@code State} — estado del comprobante (tomado del tipo de comprobante)</li>
 *   <li>{@code thirdPartyId} — identificación del tercero</li>
 *   <li>{@code thirdPartyName} — nombre del tercero (resuelto si está disponible)</li>
 *   <li>{@code account} — cuenta contable</li>
 *   <li>{@code initialBalance} — saldo de la cuenta antes del comprobante</li>
 *   <li>{@code debitMovement} — débito del asiento</li>
 *   <li>{@code creditMovement} — crédito del asiento</li>
 *   <li>{@code netMovement} — saldo neto acumulado después del asiento</li>
 * </ul>
 *
 * <p>Regla contable del saldo neto:</p>
 * <pre>
 *   Cuenta débito  → netMovement = initialBalance + debit − credit
 *   Cuenta crédito → netMovement = initialBalance − debit + credit
 * </pre>
 */
@NoArgsConstructor
public class AccountingMovementStrategy implements IProcessStrategy {

    private static final int MONEY_SCALE = 2;

    @Override
    public List<?> process(
            AuxiliaryBookCriteria criteria,
            List<AccountingInfo> allAccountingData,
            AccountingInfoProcessor accountingInfoProcessor) {

        validateCriteria(criteria);

        if (allAccountingData == null || allAccountingData.isEmpty()) {
            return Collections.emptyList();
        }

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

        LocalDate startDate = criteria.getStartDate();
        LocalDate endDate = criteria.getEndDate();

        List<AccountingInfo> validData = allAccountingData.stream()
                .filter(Objects::nonNull)
                .filter(this::hasMinimumStructure)
                .toList();

        if (validData.isEmpty()) {
            return Collections.emptyList();
        }

        // ── 1. Separar periodo anterior (saldo inicial) y periodo actual ────
        List<AccountingInfo> previousPeriodData = validData.stream()
                .filter(info -> toLocalDate(info.getDate()).isBefore(startDate))
                .toList();

        List<AccountingInfo> currentPeriodData = validData.stream()
                .filter(info -> {
                    LocalDate d = toLocalDate(info.getDate());
                    return !d.isBefore(startDate) && !d.isAfter(endDate);
                })
                .toList();

        if (currentPeriodData.isEmpty()) {
            return Collections.emptyList();
        }

        ECriteriaType criteriaType = criteria.getCriteriaType() != null
                ? criteria.getCriteriaType()
                : ECriteriaType.AUXILIARY_ACCOUNT;

        // ── 2. Calcular saldos iniciales por grupo (acumulado hasta startDate-1) ──
        Map<Long, AccountDTO> accountsByGroup = buildAccountsByGroup(validData, criteriaType);
        Map<Long, BigDecimal> initialBalanceByGroup = calculateInitialBalances(previousPeriodData, criteriaType, accountsByGroup);

        // ── 3. Agrupar el periodo actual por comprobante (tipo + número) ────
        //    Dentro de cada comprobante, ordenar por fecha y luego por código de grupo.
        Map<String, List<AccountingInfo>> byVoucher = currentPeriodData.stream()
                .collect(Collectors.groupingBy(
                        this::buildVoucherKey,
                        LinkedHashMap::new,   // preserva orden de inserción
                        Collectors.toList()
                ));

        // Ordenar los comprobantes por la fecha del primer asiento del comprobante
        List<Map.Entry<String, List<AccountingInfo>>> sortedVouchers = byVoucher.entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getValue().stream()
                        .map(AccountingInfo::getDate)
                        .min(Date::compareTo)
                        .orElse(new Date(0))))
                .toList();

        // ── 4. Construir filas del libro ────────────────────────────────────
        //    El saldo neto se acumula por grupo a lo largo de TODOS los comprobantes.
        Map<Long, BigDecimal> runningBalanceByGroup = new HashMap<>(initialBalanceByGroup);
        List<AccountingMovementBookDTO> result = new ArrayList<>();

        for (Map.Entry<String, List<AccountingInfo>> voucherEntry : sortedVouchers) {

            List<AccountingInfo> voucherMovements = voucherEntry.getValue().stream()
                    .sorted(Comparator.comparing(AccountingInfo::getDate)
                            .thenComparing(info -> extractGroupingKey(info, criteriaType))
                            .thenComparing(info -> info.getAccount().getCode()))
                    .toList();

            for (AccountingInfo info : voucherMovements) {
                Long groupCode = extractGroupingKey(info, criteriaType);
                AccountDTO accountDTO = accountsByGroup.getOrDefault(groupCode, new AccountDTO(
                        info.getAccount().getNature(),
                        groupCode,
                        normalizeText(info.getAccount().getName())
                ));

                BigDecimal currentInitialBalance = runningBalanceByGroup.getOrDefault(groupCode, BigDecimal.ZERO);
                BigDecimal debit = extractDebit(info);
                BigDecimal credit = extractCredit(info);
                String nature = accountDTO.getNature();

                BigDecimal netMovement = calculateNetBalance(currentInitialBalance, debit, credit, nature);
                runningBalanceByGroup.put(groupCode, netMovement);

                result.add(new AccountingMovementBookDTO(
                        resolveVoucherType(info),
                        formato.format(info.getDate()),
                        resolveVoucherType(info),
                        normalizeText(info.getThirdPartyId()),
                        "",
                        accountDTO,
                        scale(currentInitialBalance),
                        scale(debit),
                        scale(credit),
                        scale(netMovement)
                ));
            }
        }

        return result;
    }

    // -------------------------------------------------------------------------
    // Validation
    // -------------------------------------------------------------------------

    private void validateCriteria(AuxiliaryBookCriteria criteria) {
        if (criteria == null) {
            throw new IllegalArgumentException("Los criterios del Libro de Movimientos son obligatorios.");
        }
        if (criteria.getStartDate() == null || criteria.getEndDate() == null) {
            throw new IllegalArgumentException("El periodo es obligatorio para generar el Libro de Movimientos.");
        }
        if (criteria.getStartDate().isAfter(criteria.getEndDate())) {
            throw new IllegalArgumentException(
                    "El periodo consultado es inválido: la fecha inicial no puede ser posterior a la fecha final.");
        }
    }

    // -------------------------------------------------------------------------
    // Grouping helpers
    // -------------------------------------------------------------------------

    private Long extractGroupingKey(AccountingInfo info, ECriteriaType criteriaType) {
        String code = info.getAccount().getCode().toString();
        return Long.parseLong(switch (criteriaType) {
            case NUMBER_CLASS      -> code.substring(0, Math.min(code.length(), 1));
            case GROUP             -> code.substring(0, Math.min(code.length(), 2));
            case ACCOUNT           -> code.substring(0, Math.min(code.length(), 4));
            case SUB_ACCOUNT       -> code.substring(0, Math.min(code.length(), 6));
            case AUXILIARY_ACCOUNT -> code.substring(0, Math.min(code.length(), 8));
        });
    }

    private Map<Long, AccountDTO> buildAccountsByGroup(List<AccountingInfo> data, ECriteriaType criteriaType) {
        Map<Long, List<AccountingInfo>> grouped = data.stream()
                .collect(Collectors.groupingBy(
                        info -> extractGroupingKey(info, criteriaType),
                        TreeMap::new,
                        Collectors.toList()
                ));

        Map<Long, AccountDTO> result = new HashMap<>();
        grouped.forEach((groupCode, items) -> {
            AccountingInfo ref = items.stream()
                    .filter(info -> groupCode.toString().equals(info.getAccount().getCode().toString()))
                    .findFirst()
                    .orElseGet(() -> items.stream()
                            .min(Comparator.comparingInt((AccountingInfo i) -> i.getAccount().getCode().toString().length())
                                    .thenComparing(i -> i.getAccount().getCode()))
                            .orElse(items.get(0)));
            String description = items.stream()
                    .filter(info -> groupCode.toString().equals(info.getAccount().getCode().toString()))
                    .map(info -> info.getAccount().getName())
                    .filter(n -> n != null && !n.isBlank())
                    .findFirst()
                    .orElseGet(() -> items.stream()
                            .sorted(Comparator.comparingInt((AccountingInfo i) -> i.getAccount().getCode().toString().length())
                                    .thenComparing(i -> i.getAccount().getCode()))
                            .map(info -> info.getAccount().getName())
                            .filter(n -> n != null && !n.isBlank())
                            .findFirst()
                            .orElse(""));
            result.put(groupCode, new AccountDTO(
                    ref.getAccount().getNature(),
                    groupCode,
                    normalizeText(description)
            ));
        });
        return result;
    }

    // -------------------------------------------------------------------------
    // Balance calculation
    // -------------------------------------------------------------------------

    private Map<Long, BigDecimal> calculateInitialBalances(
            List<AccountingInfo> previousPeriodData,
            ECriteriaType criteriaType,
            Map<Long, AccountDTO> accountsByGroup) {

        Map<Long, BigDecimal> balances = new HashMap<>();

        for (AccountingInfo info : previousPeriodData) {
            Long groupCode = extractGroupingKey(info, criteriaType);
            String nature = accountsByGroup.containsKey(groupCode)
                    ? accountsByGroup.get(groupCode).getNature()
                    : info.getAccount().getNature();
            BigDecimal current = balances.getOrDefault(groupCode, BigDecimal.ZERO);
            balances.put(groupCode, calculateNetBalance(current, extractDebit(info), extractCredit(info), nature));
        }

        return balances;
    }

    private BigDecimal calculateNetBalance(BigDecimal initial, BigDecimal debit, BigDecimal credit, String nature) {
        if ("credito".equalsIgnoreCase(nature)) {
            return initial.subtract(debit).add(credit);
        }
        return initial.add(debit).subtract(credit);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private boolean hasMinimumStructure(AccountingInfo info) {
        return info.getAccount() != null
                && info.getAccount().getCode() != null
                && info.getDate() != null
                && info.getAccountingMovement() != null;
    }

    private String buildVoucherKey(AccountingInfo info) {
        String type = resolveVoucherType(info);
        String number = resolveVoucherNumber(info);
        return type + "|" + number;
    }

    private String resolveVoucherType(AccountingInfo info) {
        return info.getVoucher() != null && info.getVoucher().getType() != null
                ? normalizeText(info.getVoucher().getType())
                : "";
    }

    private String resolveVoucherNumber(AccountingInfo info) {
        return info.getVoucher() != null && info.getVoucher().getNumber() != null
                ? normalizeText(info.getVoucher().getNumber())
                : "";
    }

    private BigDecimal extractDebit(AccountingInfo info) {
        if (info.getAccountingMovement() == null || info.getAccountingMovement().getDebit() == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(info.getAccountingMovement().getDebit());
    }

    private BigDecimal extractCredit(AccountingInfo info) {
        if (info.getAccountingMovement() == null || info.getAccountingMovement().getCredit() == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(info.getAccountingMovement().getCredit());
    }

    private LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private BigDecimal scale(BigDecimal value) {
        return value.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }

    private String normalizeText(String value) {
        return value == null ? "" : value.trim();
    }
}
