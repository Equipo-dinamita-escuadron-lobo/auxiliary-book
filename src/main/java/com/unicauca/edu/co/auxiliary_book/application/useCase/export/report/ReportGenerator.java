package com.unicauca.edu.co.auxiliary_book.application.useCase.export.report;

import com.unicauca.edu.co.auxiliary_book.application.useCase.export.report.builders.ReportColumnsBuilder;
import com.unicauca.edu.co.auxiliary_book.application.useCase.export.report.builders.ReportDataBuilder;
import com.unicauca.edu.co.auxiliary_book.application.useCase.export.report.builders.ReportStyleBuilder;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.export.AuxiliaryBookTemplate;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.export.ExportInfo;
import lombok.RequiredArgsConstructor;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import org.springframework.stereotype.Service;

import java.awt.*;

@Service
@RequiredArgsConstructor
public class ReportGenerator {

    private final ReportColumnsBuilder reportColumnsBuilder;
    private final ReportDataBuilder reportDataBuilder;
    private final ReportStyleBuilder reportStyleBuilder;


    public JasperReportBuilder generate(ExportInfo exportInfo) {
        try{
            AuxiliaryBookTemplate template = exportInfo.getInfoReportTemplate();

            JasperReportBuilder report = DynamicReports.report();

            //Style
            this.reportStyleBuilder.templateBuilder(report, template, exportInfo);

            // Columns
            this.reportColumnsBuilder.setColumnsReport(report, exportInfo.getAuxBookType());

            // Data source
            this.reportDataBuilder.setDataSource(report, exportInfo);

            return report;
        } catch (Exception e) {
            throw new RuntimeException("Error generating report", e);
        }
    }
}
