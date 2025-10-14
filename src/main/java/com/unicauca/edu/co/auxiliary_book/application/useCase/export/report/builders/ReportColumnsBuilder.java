package com.unicauca.edu.co.auxiliary_book.application.useCase.export.report.builders;

import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookType;
import lombok.NoArgsConstructor;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.grid.ColumnTitleGroupBuilder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@NoArgsConstructor
public class ReportColumnsBuilder {

    //Initializing common columns in multiple reports
    //Date column
    private final TextColumnBuilder<String> dateCol = DynamicReports.col.column("Fecha", "date", DynamicReports.type.stringType());

    //Account related columns
    private final TextColumnBuilder<String> accountCodeCol = DynamicReports.col.column("Código", "accountCode", DynamicReports.type.stringType());
    private final TextColumnBuilder<String> accountDescriptionCol = DynamicReports.col.column("Descripción", "accountDescription", DynamicReports.type.stringType());
    private final ColumnTitleGroupBuilder titleAccountGroup = DynamicReports.grid
            .titleGroup(
                    "Cuenta",
                    this.accountCodeCol,
                    this.accountDescriptionCol
            );

    //Third party related columns
    private final TextColumnBuilder<String> thirdPartyIdCol = DynamicReports.col.column("Numero de Identificación", "thirdPartyId", DynamicReports.type.stringType());
    private final TextColumnBuilder<String> thirdPartyNameCol = DynamicReports.col.column("Nombre", "thirdPartyName", DynamicReports.type.stringType());
    private final ColumnTitleGroupBuilder titleThirdPartyGroup = DynamicReports.grid
            .titleGroup(
                    "Tercero",
                    this.thirdPartyIdCol,
                    this.thirdPartyNameCol
            );


    public void setColumnsReport(JasperReportBuilder report, EAuxiliaryBookType type){
        switch (type) {
            case INVENTORY_AND_BALANCES->this.setColumnsInventoryAndBalancesReport(report);
            case DIARY -> this.setColumnsDiaryReport(report);
            case MAJOR_AND_BALANCES -> this.setColumnsMajorAndBalancesReport(report);
            case ACCOUNT, THIRD_PARTY -> this.setColumnsAccountAndThirdPartyReport(report);
            case ACCOUNTING_MOVEMENT -> this.setColumnsAccountingMovementReport(report);
            default -> throw new IllegalArgumentException("Unsupported auxiliary book type: " + type);
        }
    }


    private void setColumnsInventoryAndBalancesReport(JasperReportBuilder report) {
        TextColumnBuilder<String> description = DynamicReports.col.column("Descripcion", "description", DynamicReports.type.stringType());
        TextColumnBuilder<BigDecimal> valor = DynamicReports.col.column("Valor", "value", DynamicReports.type.bigDecimalType());

        report.columnGrid(this.titleAccountGroup, description, valor);
        report.columns(this.accountCodeCol, this.accountDescriptionCol, description, valor);
    }


    private void setColumnsDiaryReport(JasperReportBuilder report) {
        TextColumnBuilder<String> voucherNameCol = DynamicReports.col.column("Comprobante", "voucherName", DynamicReports.type.stringType());
        TextColumnBuilder<String> voucherNumberCol = DynamicReports.col.column("Número", "voucherNumber", DynamicReports.type.stringType());

        TextColumnBuilder<BigDecimal> debitCol = DynamicReports.col.column("Débito", "debit", DynamicReports.type.bigDecimalType());
        TextColumnBuilder<BigDecimal> creditCol = DynamicReports.col.column("Crédito", "credit", DynamicReports.type.bigDecimalType());


        ColumnTitleGroupBuilder titleVoucherGroup = DynamicReports.grid.titleGroup("Comprobante", voucherNameCol, voucherNumberCol);

        report.columnGrid(dateCol, titleAccountGroup, titleVoucherGroup, debitCol, creditCol);
        report.columns(dateCol, accountCodeCol, accountDescriptionCol, voucherNameCol, voucherNumberCol, debitCol, creditCol);
    }

    private void setColumnsMajorAndBalancesReport(JasperReportBuilder report) {
        TextColumnBuilder<BigDecimal> initialBalanceCol = DynamicReports.col.column("Saldo Inicial", "initialBalance", DynamicReports.type.bigDecimalType());
        TextColumnBuilder<BigDecimal> debitCol = DynamicReports.col.column("Débito", "debit", DynamicReports.type.bigDecimalType());
        TextColumnBuilder<BigDecimal> creditCol = DynamicReports.col.column("Crédito", "credit", DynamicReports.type.bigDecimalType());
        TextColumnBuilder<BigDecimal> finalBalanceCol = DynamicReports.col.column("Saldo Final", "finalBalance", DynamicReports.type.bigDecimalType());
        
        ColumnTitleGroupBuilder titleMovementGroup = DynamicReports.grid.titleGroup("Movimiento", debitCol, creditCol);
        
        report.columnGrid(this.titleAccountGroup, initialBalanceCol, titleMovementGroup, finalBalanceCol);
        report.columns(this.accountCodeCol, this.accountDescriptionCol, initialBalanceCol, debitCol, creditCol, finalBalanceCol);
    }

    private void setColumnsAccountAndThirdPartyReport(JasperReportBuilder report) {
        TextColumnBuilder<BigDecimal> debitCol = DynamicReports.col.column("Débito", "debitMovement", DynamicReports.type.bigDecimalType());
        TextColumnBuilder<BigDecimal> creditCol = DynamicReports.col.column("Crédito", "creditMovement", DynamicReports.type.bigDecimalType());
        TextColumnBuilder<BigDecimal> balanceCol = DynamicReports.col.column("Saldo", "balanceMovement", DynamicReports.type.bigDecimalType());
        TextColumnBuilder<String> voucherCostCenterCol = DynamicReports.col.column("Centro de Costo", "voucherCostCenter", DynamicReports.type.stringType());
        TextColumnBuilder<String> voucherNumberCol = DynamicReports.col.column("Número", "voucherNumber", DynamicReports.type.stringType());

        ColumnTitleGroupBuilder titleMovementGroup = DynamicReports.grid.titleGroup("Movimiento", debitCol, creditCol, balanceCol);
        ColumnTitleGroupBuilder titleVoucherGroup = DynamicReports.grid.titleGroup("Documento", voucherCostCenterCol, voucherNumberCol);

        report.columnGrid(dateCol, this.titleAccountGroup, titleMovementGroup, this.titleThirdPartyGroup, titleVoucherGroup);
        report.columns(dateCol, this.accountCodeCol, this.accountDescriptionCol, this.thirdPartyIdCol, this.thirdPartyNameCol,
                voucherCostCenterCol, voucherNumberCol, debitCol, creditCol, balanceCol);
    }

    private void setColumnsAccountingMovementReport(JasperReportBuilder report) {
        TextColumnBuilder<String> voucherTypeCol = DynamicReports.col.column("Tipo Documento", "voucherType", DynamicReports.type.stringType());
        TextColumnBuilder<String> voucherStateCol = DynamicReports.col.column("Estado", "voucherState", DynamicReports.type.stringType());
        TextColumnBuilder<BigDecimal> initialBalanceCol = DynamicReports.col.column("Saldo Inicial", "initialBalance", DynamicReports.type.bigDecimalType());
        TextColumnBuilder<BigDecimal> debitCol = DynamicReports.col.column("Débito", "debitMovement", DynamicReports.type.bigDecimalType());
        TextColumnBuilder<BigDecimal> creditCol = DynamicReports.col.column("Crédito", "creditMovement", DynamicReports.type.bigDecimalType());
        TextColumnBuilder<BigDecimal> netMovementCol = DynamicReports.col.column("Movimiento Neto", "netMovement", DynamicReports.type.bigDecimalType());

        ColumnTitleGroupBuilder titleMovementGroup = DynamicReports.grid.titleGroup("Movimiento", initialBalanceCol, debitCol, creditCol);

        report.columnGrid(voucherTypeCol, this.dateCol, voucherStateCol, this.titleThirdPartyGroup, this.titleAccountGroup, titleMovementGroup, netMovementCol);

        report.columns(voucherTypeCol, this.dateCol, voucherStateCol, this.thirdPartyIdCol, this.thirdPartyNameCol,
                this.accountCodeCol, this.accountDescriptionCol, initialBalanceCol, debitCol, creditCol, netMovementCol);
    }

}
