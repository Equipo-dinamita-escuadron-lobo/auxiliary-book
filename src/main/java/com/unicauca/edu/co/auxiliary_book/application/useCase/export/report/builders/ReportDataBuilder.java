package com.unicauca.edu.co.auxiliary_book.application.useCase.export.report.builders;

import com.unicauca.edu.co.auxiliary_book.application.dto.auxiliaryBook.*;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.export.ExportInfo;
import lombok.NoArgsConstructor;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;

@Service
@NoArgsConstructor
public class ReportDataBuilder {

    private static final DateTimeFormatter JSON_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void setDataSource(JasperReportBuilder report, ExportInfo exportInfo) {
        List<?> typedData;

        switch(exportInfo.getAuxiliaryBook().getType()){
            case INVENTORY_AND_BALANCES:
                typedData = mapAuxBookData(exportInfo.getAuxBookData(), map -> {
                            // 1. Extraer el mapa de la cuenta anidada
                            @SuppressWarnings("unchecked")
                            LinkedHashMap<String, Object> accountMap = (LinkedHashMap<String, Object>) map.get("account");

                            System.out.println("DEBUG (Datos del Mapa): " + map);

                            // 2. Crear el AccountDTO
                            AccountDTO accountDto = new AccountDTO(
                                    (String) accountMap.get("nature"),
                                    safeParseLong(accountMap.get("accountCode")),
                                    (String) accountMap.get("accountDescription")
                            );

                            // 3. Crear el DTO principal
                            return new InventoryAndBalancesBookDTO(
                                    accountDto,
                                    (String) map.get("description"),
                                    safeParseBigDecimal(map.get("value"))
                            );
                        }
                );
                break;
            case DIARY:
                typedData = mapAuxBookData(exportInfo.getAuxBookData(), map -> {
                            @SuppressWarnings("unchecked")
                            LinkedHashMap<String, Object> accountMap = (LinkedHashMap<String, Object>) map.get("account");

                            AccountDTO accountDto = new AccountDTO(
                                    (String) accountMap.get("nature"),
                                    safeParseLong(accountMap.get("accountCode")),
                                    (String) accountMap.get("accountDescription")
                            );

                            return new DiaryBookDTO(
                                    safeParseDate(map.get("date")),
                                    accountDto,
                                    (String) map.get("voucherName"),
                                    (String) map.get("voucherNumber"),
                                    safeParseBigDecimal(map.get("debit")),
                                    safeParseBigDecimal(map.get("credit"))
                            );
                        }
                );
                break;
            case MAJOR_AND_BALANCES:
                typedData = mapAuxBookData(exportInfo.getAuxBookData(), map -> {
                            @SuppressWarnings("unchecked")
                            LinkedHashMap<String, Object> accountMap = (LinkedHashMap<String, Object>) map.get("account");

                            AccountDTO accountDto = new AccountDTO(
                                    (String) accountMap.get("nature"),
                                    safeParseLong(accountMap.get("accountCode")),
                                    (String) accountMap.get("accountDescription")
                            );

                            return new MajorAndBalancesBookDTO(
                                    accountDto,
                                    safeParseBigDecimal(map.get("initialBalance")),
                                    safeParseBigDecimal(map.get("debitMovement")),
                                    safeParseBigDecimal(map.get("creditMovement")),
                                    safeParseBigDecimal(map.get("finalBalance"))
                            );
                        }
                );
                break;
            case ACCOUNT:
                typedData = mapAuxBookData(exportInfo.getAuxBookData(), map -> {
                            @SuppressWarnings("unchecked")
                            LinkedHashMap<String, Object> accountMap = (LinkedHashMap<String, Object>) map.get("account");

                            AccountDTO accountDto = new AccountDTO(
                                    (String) accountMap.get("nature"),
                                    safeParseLong(accountMap.get("accountCode")),
                                    (String) accountMap.get("accountDescription")
                            );

                            return new AccountBookDTO(
                                    safeParseDate(map.get("date")),
                                    accountDto,
                                    safeParseBigDecimal(map.get("debitMovement")),
                                    safeParseBigDecimal(map.get("creditMovement")),
                                    safeParseBigDecimal(map.get("balanceMovement")),
                                    (String) map.get("thirdPartyId"),
                                    (String) map.get("thirdPartyName"),
                                    (String) map.get("voucherCostCenter"),
                                    (String) map.get("voucherNumber")
                            );
                        }
                );
                break;
            case THIRD_PARTY:
                // Asumiendo que ThirdPartyBookDTO también tiene un AccountDTO anidado
                typedData = mapAuxBookData(exportInfo.getAuxBookData(), map -> {
                            @SuppressWarnings("unchecked")
                            LinkedHashMap<String, Object> accountMap = (LinkedHashMap<String, Object>) map.get("account");

                            AccountDTO accountDto = new AccountDTO(
                                    (String) accountMap.get("nature"),
                                    safeParseLong(accountMap.get("accountCode")),
                                    (String) accountMap.get("accountDescription")
                            );

                            return new ThirdPartyBookDTO(
                                    safeParseDate(map.get("date")),
                                    accountDto,
                                    safeParseBigDecimal(map.get("debitMovement")),
                                    safeParseBigDecimal(map.get("creditMovement")),
                                    safeParseBigDecimal(map.get("balanceMovement")),
                                    (String) map.get("thirdPartyId"),
                                    (String) map.get("thirdPartyName"),
                                    (String) map.get("voucherCostCenter"),
                                    (String) map.get("voucherNumber")
                            );
                        }
                );
                break;
            case ACCOUNTING_MOVEMENT:
                // Asumiendo que AccountingMovementBookDTO también tiene un AccountDTO anidado
                typedData = mapAuxBookData(exportInfo.getAuxBookData(), map -> {
                            @SuppressWarnings("unchecked")
                            LinkedHashMap<String, Object> accountMap = (LinkedHashMap<String, Object>) map.get("account");

                            AccountDTO accountDto = new AccountDTO(
                                    (String) accountMap.get("nature"),
                                    safeParseLong(accountMap.get("accountCode")),
                                    (String) accountMap.get("accountDescription")
                            );

                            return new AccountingMovementBookDTO(
                                    (String) map.get("voucherType"),
                                    safeParseDate(map.get("date")),
                                    (String) map.get("State"),
                                    (String) map.get("thirdPartyId"),
                                    (String) map.get("thirdPartyName"),
                                    accountDto, // Campo de cuenta anidado
                                    safeParseBigDecimal(map.get("initialBalance")),
                                    safeParseBigDecimal(map.get("debitMovement")),
                                    safeParseBigDecimal(map.get("creditMovement")),
                                    safeParseBigDecimal(map.get("netMovement"))
                            );
                        }
                );
                break;
            default:
                throw new IllegalArgumentException("Unsupported auxiliary book type: " + exportInfo.getAuxiliaryBook().getType());
        }

        JRDataSource dataSource = new JRBeanCollectionDataSource(typedData);
        report.setDataSource(dataSource);

    }

    /**
     * Mapea una lista de datos genéricos (usualmente LinkedHashMap) a una lista de DTOs tipados.
     */
    private <T> List<T> mapAuxBookData(List<?> rawData, Function<LinkedHashMap<String, Object>, T> mapper) {
        return rawData.stream()
                .map(item -> {
                    if (!(item instanceof LinkedHashMap<?,?> rawMap)) {
                        throw new IllegalArgumentException("Unexpected data type: " + item.getClass());
                    }
                    @SuppressWarnings("unchecked")
                    LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) rawMap;
                    return mapper.apply(map);
                })
                .toList();
    }

    /**
     * Convierte de forma segura un Objeto (que puede ser String, Integer, Long, etc.) a Long.
     */
    private Long safeParseLong(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Long) {
            return (Long) obj;
        }
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        }
        try {
            return Long.parseLong(obj.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Convierte de forma segura un Objeto (que puede ser String, Double, etc.) a BigDecimal.
     */
    private BigDecimal safeParseBigDecimal(Object obj) {
        if (obj == null) {
            return null; // O BigDecimal.ZERO si la lógica de negocio lo prefiere
        }
        if (obj instanceof BigDecimal) {
            return (BigDecimal) obj;
        }
        try {
            return new BigDecimal(obj.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Convierte de forma segura un Objeto (String, LocalDate, o Date) a un java.util.Date.
     */
    private Date safeParseDate(Object obj) {
        if (obj == null) {
            return null;
        }

        // Caso 1: El objeto ya es un java.util.Date
        if (obj instanceof Date) {
            return (Date) obj;
        }

        // Caso 2: El objeto es un java.time.LocalDate
        if (obj instanceof LocalDate) {
            return java.sql.Date.valueOf((LocalDate) obj);
        }

        // Caso 3: El objeto es un String (como en tu JSON "2025-01-01")
        if (obj instanceof String) {
            try {
                // Usamos el formato "yyyy-MM-dd" que vimos en tu JSON
                LocalDate localDate = LocalDate.parse(obj.toString(), JSON_DATE_FORMATTER);
                return java.sql.Date.valueOf(localDate);
            } catch (DateTimeParseException e) {
                System.err.println("Error parseando fecha (formato no reconocido): " + obj);
                return null;
            }
        }

        System.err.println("Tipo de fecha inesperado (" + obj.getClass().getName() + "): " + obj);
        return null;
    }
}
