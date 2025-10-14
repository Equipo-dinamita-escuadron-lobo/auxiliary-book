package com.unicauca.edu.co.auxiliary_book.application.useCase.export.report.builders;

import com.unicauca.edu.co.auxiliary_book.application.dto.*;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.export.ExportInfo;
import lombok.NoArgsConstructor;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;

@Service
@NoArgsConstructor
public class ReportDataBuilder {

    public void setDataSource(JasperReportBuilder report, ExportInfo exportInfo) {

        List<?> typedData = null;

        switch(exportInfo.getAuxBookType()){
            case INVENTORY_AND_BALANCES:
                typedData = mapAuxBookData(exportInfo.getAuxBookData(), map ->
                        new InventoryAndBalancesBookDTO(
                                (String) map.get("accountCode"),
                                (String) map.get("accountDescription"),
                                (String) map.get("description"),
                                new BigDecimal(map.get("value").toString())
                        )
                );
                break;
            case DIARY:
                typedData = mapAuxBookData(exportInfo.getAuxBookData(), map ->
                        new DiaryBookDTO(
                                (LocalDate) map.get("date"),
                                (String) map.get("accountCode"),
                                (String) map.get("accountDescription"),
                                (String) map.get("voucherName"),
                                (String) map.get("voucherNumber"),
                                new BigDecimal(map.get("debit").toString()),
                                new BigDecimal(map.get("credit").toString())
                        )
                );
                break;
            case MAJOR_AND_BALANCES:
                typedData = mapAuxBookData(exportInfo.getAuxBookData(), map ->
                        new MajorAndBalancesBookDTO(
                                (String) map.get("accountCode"),
                                (String) map.get("accountDescription"),
                                new BigDecimal(map.get("initialBalance").toString()),
                                new BigDecimal(map.get("debitMovement").toString()),
                                new BigDecimal(map.get("creditMovement").toString()),
                                new BigDecimal(map.get("finalBalance").toString())
                        )
                );
                break;
            case ACCOUNT:
                typedData = mapAuxBookData(exportInfo.getAuxBookData(), map ->
                        new AccountBookDTO(
                                (LocalDate) map.get("date"),
                                (String) map.get("accountCode"),
                                (String) map.get("accountDescription"),
                                new BigDecimal(map.get("debitMovement").toString()),
                                new BigDecimal(map.get("creditMovement").toString()),
                                new BigDecimal(map.get("balanceMovement").toString()),
                                (String) map.get("thirdPartyId"),
                                (String) map.get("thirdPartyName"),
                                (String) map.get("voucherCostCenter"),
                                (String) map.get("voucherNumber")
                        )
                );
                break;
            case THIRD_PARTY:
                typedData = mapAuxBookData(exportInfo.getAuxBookData(), map ->
                        new ThirdPartyBookDTO(
                                (LocalDate) map.get("date"),
                                (String) map.get("accountCode"),
                                (String) map.get("accountDescription"),
                                new BigDecimal(map.get("debitMovement").toString()),
                                new BigDecimal(map.get("creditMovement").toString()),
                                new BigDecimal(map.get("balanceMovement").toString()),
                                (String) map.get("thirdPartyId"),
                                (String) map.get("thirdPartyName"),
                                (String) map.get("voucherCostCenter"),
                                (String) map.get("voucherNumber")
                        )
                );
                break;
            case ACCOUNTING_MOVEMENT:
                typedData = mapAuxBookData(exportInfo.getAuxBookData(), map ->
                        new AccountingMovementBookDTO(
                                (String) map.get("voucherType"),
                                (LocalDate) map.get("date"),
                                (String) map.get("State"),
                                (String) map.get("thirdPartyId"),
                                (String) map.get("thirdPartyName"),
                                (String) map.get("accountCode"),
                                (String) map.get("accountDescription"),
                                new BigDecimal(map.get("initialBalance").toString()),
                                new BigDecimal(map.get("debitMovement").toString()),
                                new BigDecimal(map.get("creditMovement").toString()),
                                new BigDecimal(map.get("netMovement").toString())
                        )
                );
                break;
            default:
                throw new IllegalArgumentException("Unsupported auxiliary book type: " + exportInfo.getAuxBookType());
        }

        JRDataSource dataSource = new JRBeanCollectionDataSource(typedData);
        report.setDataSource(dataSource);

    }

    private <T> List<T> mapAuxBookData(List<?> rawData, Function<LinkedHashMap<?,?>, T> mapper) {
        return rawData.stream()
                .map(item -> {
                    if (!(item instanceof LinkedHashMap<?,?> map)) {
                        throw new IllegalArgumentException("Unexpected data type: " + item.getClass());
                    }
                    return mapper.apply(map);
                })
                .toList();
    }

}
