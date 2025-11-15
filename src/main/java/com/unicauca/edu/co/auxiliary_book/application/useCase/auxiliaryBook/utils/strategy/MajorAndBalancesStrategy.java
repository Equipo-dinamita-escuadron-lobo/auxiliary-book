package com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

import com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils.AccountingInfoProcessor;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.AuxiliaryBookCriteria;
import com.unicauca.edu.co.auxiliary_book.domain.models.external.accountingInfo.AccountingInfo;

import lombok.NoArgsConstructor;

import java.util.stream.Collectors;

import com.unicauca.edu.co.auxiliary_book.application.dto.MajorAndBalancesBookDTO;

@NoArgsConstructor
/**
 * Estrategia para la generación del reporte de Libro Mayor.
 *
 * Esta clase implementa la lógica de negocio para transformar una lista de movimientos contables
 * en un reporte de Libro Mayor, agrupando por cuenta, calculando saldos y aplicando
 * las reglas contables según la naturaleza de cada cuenta.
 */
public class MajorAndBalancesStrategy implements IProcessStrategy {

    /**
     * Procesa los datos contables para generar el Libro Mayor.
     *
     * @param criteria Criterios de filtrado (ej. rango de fechas). No se usa directamente aquí, pero está disponible.
     * @param data Lista de información contable (movimientos) a procesar.
     * @param accountingInfoProcessor Procesador de utilidades (no utilizado en esta implementación específica).
     * @return Una lista de DTOs {@link MajorAndBalancesBookDTO} que representa el Libro Mayor.
     */
    @Override
    public List<?> process(AuxiliaryBookCriteria criteria, List<AccountingInfo> allAccountingData, AccountingInfoProcessor accountingInfoProcessor) {

        LocalDate reportStartDate = criteria.getStartDate();
        if (reportStartDate == null) {
            return Collections.emptyList(); // Es necesario para la división de datos.
        }

        // 1. Dividir la lista completa en dos: movimientos anteriores y movimientos del periodo actual.
        List<AccountingInfo> previousPeriodData = allAccountingData.stream()
                .filter(info -> info.getDate() != null && info.getDate().isBefore(reportStartDate))
                .toList();

        List<AccountingInfo> currentPeriodData = allAccountingData.stream()
                .filter(info -> info.getDate() != null && !info.getDate().isBefore(reportStartDate))
                .toList();

        // 2. Calcular los saldos iniciales usando los datos del periodo anterior.
        Map<AccountKey, BigDecimal> initialBalances = calculateFinalBalancesForPeriod(previousPeriodData);

        // 3. Agrupar los movimientos del PERIODO ACTUAL por cuenta.
        Map<AccountKey, List<AccountingInfo>> movementsByAccount = currentPeriodData.stream()
                .collect(Collectors.groupingBy(info -> new AccountKey(
                        info.getAccount().getCode(),
                        info.getAccount().getName(),
                        info.getAccount().getNature()
                )));

        // 4. Consolidar todas las cuentas únicas (las que tienen saldo inicial + las que tienen movimiento actual).
        Set<AccountKey> allAccountKeys = new HashSet<>(initialBalances.keySet());
        allAccountKeys.addAll(movementsByAccount.keySet());

        List<MajorAndBalancesBookDTO> majorBookEntries = new ArrayList<>();

        // 5. Iterar sobre CADA cuenta para construir la entrada del libro mayor.
        for (AccountKey accountKey : allAccountKeys) {
            // Obtener el saldo inicial. Será 0 si la cuenta es nueva en este periodo.
            BigDecimal initialBalance = initialBalances.getOrDefault(accountKey, BigDecimal.ZERO);

            // Obtener los movimientos del periodo actual. Será una lista vacía si no hubo movimientos.
            List<AccountingInfo> currentMovements = movementsByAccount.getOrDefault(accountKey, Collections.emptyList());

            // Calcular débitos y créditos del periodo actual.
            BigDecimal totalDebit = currentMovements.stream()
                    .map(info -> info.getAccountingMovement().getDebit())
                    .filter(Objects::nonNull)
                    .map(BigDecimal::valueOf)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalCredit = currentMovements.stream()
                    .map(info -> info.getAccountingMovement().getCredit())
                    .filter(Objects::nonNull)
                    .map(BigDecimal::valueOf)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Calcular el saldo final.
            BigDecimal finalBalance = calculateFinalBalance(initialBalance, totalDebit, totalCredit, accountKey.nature());

            // Construir y añadir el DTO.
            MajorAndBalancesBookDTO dto = new MajorAndBalancesBookDTO(
                    accountKey.code().toString(),
                    accountKey.description(),
                    initialBalance.setScale(2, RoundingMode.HALF_UP),
                    totalDebit.setScale(2, RoundingMode.HALF_UP),
                    totalCredit.setScale(2, RoundingMode.HALF_UP),
                    finalBalance.setScale(2, RoundingMode.HALF_UP)
            );
            majorBookEntries.add(dto);
        }

        // 6. Ordenar los resultados por código de cuenta de forma ascendente.
        majorBookEntries.sort(Comparator.comparing(MajorAndBalancesBookDTO::getAccountCode));

        return majorBookEntries;
    }

    /**
     * Método reutilizable que calcula el saldo final para un conjunto de movimientos contables.
     *
     * @param data Lista de movimientos contables de cualquier periodo.
     * @return Un mapa que asocia cada cuenta con su saldo final calculado.
     */
    private Map<AccountKey, BigDecimal> calculateFinalBalancesForPeriod(List<AccountingInfo> data) {
        if (data == null || data.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<AccountKey, List<AccountingInfo>> groupedData = data.stream()
                .collect(Collectors.groupingBy(info -> new AccountKey(
                        info.getAccount().getCode(),
                        info.getAccount().getName(),
                        info.getAccount().getNature()
                )));

        return groupedData.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            List<AccountingInfo> movements = entry.getValue();
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

                            // El saldo inicial para este cálculo acumulado siempre es CERO.
                            return calculateFinalBalance(BigDecimal.ZERO, totalDebit, totalCredit, entry.getKey().nature());
                        }
                ));
    }


    /**
     * Calcula el saldo final basado en la naturaleza de la cuenta.
     *
     * @param initialBalance Saldo inicial.
     * @param totalDebit Suma de todos los débitos.
     * @param totalCredit Suma de todos los créditos.
     * @param nature Naturaleza de la cuenta ("debito" o "credito").
     * @return El saldo final calculado.
     */
    private BigDecimal calculateFinalBalance(BigDecimal initialBalance, BigDecimal totalDebit, BigDecimal totalCredit, String nature) {
        if ("debito".equalsIgnoreCase(nature)) {
            // Naturaleza Deudora: Saldo Final = Saldo Inicial + Débitos - Créditos
            return initialBalance.add(totalDebit).subtract(totalCredit);
        } else if ("credito".equalsIgnoreCase(nature)) {
            // Naturaleza Acreedora: Saldo Final = Saldo Inicial - Débitos + Créditos
            return initialBalance.subtract(totalDebit).add(totalCredit);
        }
        return initialBalance.add(totalDebit).subtract(totalCredit);
    }

    /**
     * Record local para actuar como una clave de agrupación inmutable y concisa.
     */
    private record AccountKey(Long code, String description, String nature) {}
}
