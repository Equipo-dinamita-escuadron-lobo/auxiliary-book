package com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils.strategy;

import com.unicauca.edu.co.auxiliary_book.application.dto.AccountBookDTO;
import com.unicauca.edu.co.auxiliary_book.application.dto.AccountDTO;
import com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils.AccountingInfoProcessor;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.AuxiliaryBookCriteria;
import com.unicauca.edu.co.auxiliary_book.domain.models.external.accountingInfo.AccountingInfo;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

@NoArgsConstructor
public class AccountStrategy implements IProcessStrategy {

    /**
     * Procesa los datos contables para generar el reporte de Libro Auxiliar por Cuenta.
     * Este reporte es cronológico y calcula un saldo acumulado por cada transacción
     * para una cuenta específica.
     *
     * @param criteria Criterios de filtrado. Se asume que contiene la fecha de inicio
     * y el código de la cuenta a filtrar (en getAccountRange().getFromRange()).
     * @param allAccountingData Lista COMPLETA de información contable a procesar.
     * @param accountingInfoProcessor Procesador de utilidades (no utilizado).
     * @return Una lista de DTOs {@link AccountBookDTO} que representa el libro auxiliar.
     */
    @Override
    public List<?> process(AuxiliaryBookCriteria criteria, List<AccountingInfo> allAccountingData, AccountingInfoProcessor accountingInfoProcessor) {

        // --- 1. Obtener los parámetros de filtrado ---

        // Asumimos que para este reporte, getFromRange() contiene el CÓDIGO de la cuenta.
        String accountCodeToFilter;
        if (criteria.getCriteriaRange() == null || criteria.getCriteriaRange().getFromRange() == null) {
            System.err.println("AccountStrategy: No se especificó un código de cuenta en los criterios (criteria.getAccountRange().getFromRange()).");
            return Collections.emptyList(); // No se puede generar el reporte sin una cuenta
        }
        accountCodeToFilter = criteria.getCriteriaRange().getFromRange().toString();

        // --- CORRECCIÓN: Convertir LocalDate (criterio) a java.util.Date (datos) ---
        LocalDate reportStartDateLocal = criteria.getStartDate();
        if (reportStartDateLocal == null) {
            System.err.println("AccountStrategy: No se especificó una fecha de inicio en los criterios.");
            return Collections.emptyList();
        }
        // Convertimos el LocalDate a un java.util.Date (al inicio del día)
        java.util.Date reportStartDate = java.sql.Date.valueOf(reportStartDateLocal.toString());
        // --- FIN CORRECCIÓN ---


        // --- 2. Filtrar TODOS los datos solo para esta cuenta ---
        List<AccountingInfo> allDataForThisAccount = allAccountingData.stream()
                .filter(info -> info != null && info.getAccount() != null && info.getAccount().getCode() != null)
                .filter(info -> info.getAccount().getCode().toString().equals(accountCodeToFilter))
                .toList();

        if (allDataForThisAccount.isEmpty()) {
            return Collections.emptyList(); // No hay datos para esta cuenta
        }

        // --- 3. Dividir la lista de la cuenta en dos: anterior y actual ---

        // --- CORRECCIÓN: Usar .before() para comparar java.util.Date ---
        List<AccountingInfo> previousPeriodData = allDataForThisAccount.stream()
                .filter(info -> info.getDate() != null && info.getDate().before(reportStartDate))
                .toList();

        List<AccountingInfo> currentPeriodData = allDataForThisAccount.stream()
                .filter(info -> info.getDate() != null && !info.getDate().before(reportStartDate)) // <-- Lógica (NOT before) es correcta
                // ¡Importante! Ordenar cronológicamente para el saldo acumulado
                .sorted(Comparator.comparing(AccountingInfo::getDate)
                        .thenComparing(info -> info.getVoucher() != null ? info.getVoucher().getNumber() : "")) // <-- Añadido control de nulos
                .toList();
        // --- FIN CORRECCIÓN ---

        // --- 4. Calcular Saldo Inicial ---
        // Se calcula con todos los movimientos ANTERIORES al periodo del reporte
        String accountNature = allDataForThisAccount.get(0).getAccount().getNature();
        BigDecimal runningBalance = calculateBalanceForPeriod(previousPeriodData, accountNature); // Esta será nuestra variable de saldo acumulado

        List<AccountBookDTO> results = new ArrayList<>();

        // --- 5. Iterar sobre el periodo actual y calcular el saldo línea a línea ---
        for (AccountingInfo info : currentPeriodData) {

            // Usamos .valueOf() y manejamos nulos defensivamente
            Double debitValue = Optional.ofNullable(info.getAccountingMovement())
                    .map(acc -> acc.getDebit())
                    .orElse(0.0);
            Double creditValue = Optional.ofNullable(info.getAccountingMovement())
                    .map(acc -> acc.getCredit())
                    .orElse(0.0);

            BigDecimal debit = BigDecimal.valueOf(debitValue);
            BigDecimal credit = BigDecimal.valueOf(creditValue);

            // Calcular el saldo acumulado para esta transacción
            runningBalance = calculateFinalBalance(runningBalance, debit, credit, accountNature);

            // Crear el AccountDTO
            AccountDTO accountDTO = new AccountDTO(
                    accountNature,
                    info.getAccount().getCode(),
                    info.getAccount().getName()
            );

            String costCenterCode = (info.getCostCenter() != null && info.getCostCenter().getCode() != null)
                    ? info.getCostCenter().getCode() : null;

            String voucherNumber = (info.getVoucher() != null)
                    ? String.valueOf(info.getVoucher().getNumber()) : null;

            // Crear el DTO del reporte
            // (Asumimos que AccountBookDTO espera java.util.Date)
            AccountBookDTO dto = new AccountBookDTO(
                    info.getDate(), // info.getDate() es java.util.Date
                    accountDTO,
                    debit.setScale(2, RoundingMode.HALF_UP),
                    credit.setScale(2, RoundingMode.HALF_UP),
                    runningBalance.setScale(2, RoundingMode.HALF_UP), // El saldo acumulado
                    info.getThirdPartyId(),
                    null, // Nombre de tercero no disponible en la fuente actual
                    costCenterCode,
                    voucherNumber
            );

            results.add(dto);
        }

        return results;
    }

    /**
     * Calcula el saldo final para un conjunto de datos (usado para el saldo inicial).
     *
     * @param data Lista de movimientos contables (ya filtrada por cuenta).
     * @param nature La naturaleza de la cuenta.
     * @return El saldo final del periodo.
     */
    private BigDecimal calculateBalanceForPeriod(List<AccountingInfo> data, String nature) {
        if (data == null || data.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalDebit = data.stream()
                .map(info -> info.getAccountingMovement().getDebit())
                .filter(Objects::nonNull)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCredit = data.stream()
                .map(info -> info.getAccountingMovement().getCredit())
                .filter(Objects::nonNull)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // El saldo inicial para este cálculo acumulado siempre es CERO.
        return calculateFinalBalance(BigDecimal.ZERO, totalDebit, totalCredit, nature);
    }

    /**
     * Calcula el saldo final basado en la naturaleza de la cuenta.
     *
     * @param currentBalance Saldo actual (o inicial).
     * @param debit Monto débito de la transacción actual.
     * @param credit Monto crédito de la transacción actual.
     * @param nature Naturaleza de la cuenta ("debito" o "credito").
     * @return El saldo final calculado.
     */
    private BigDecimal calculateFinalBalance(BigDecimal currentBalance, BigDecimal debit, BigDecimal credit, String nature) {
        BigDecimal subtract = currentBalance.add(debit).subtract(credit);
        if ("credito".equalsIgnoreCase(nature)) {
            // Naturaleza Acreedora: Saldo Final = Saldo Actual - Débitos + Créditos
            return currentBalance.subtract(debit).add(credit);
        }
        // Default a debito si la naturaleza es nula o desconocida
        return subtract;
    }
}
