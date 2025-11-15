package com.unicauca.edu.co.auxiliary_book.application.useCase.export;

import com.unicauca.edu.co.auxiliary_book.application.ports.in.export.IExportReportPort;
import com.unicauca.edu.co.auxiliary_book.application.useCase.export.report.ReportGenerator;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.export.ExportInfo;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookFormat;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookType;
import lombok.RequiredArgsConstructor;
import net.sf.dynamicreports.report.exception.DRException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class ExportAuxiliaryBookUC implements IExportReportPort {

    private final ReportGenerator reportGenerator;

    @Override
    public byte[] exportReport(ExportInfo exportInfo) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            switch (exportInfo.getFormat()) {
                case PDF:
                    reportGenerator.generate(exportInfo).toPdf(baos);
                    break;
                case EXCEL:
                    reportGenerator.generate(exportInfo).toXls(baos);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported format: " + exportInfo.getFormat());
            }
        } catch (DRException e) {
            throw new RuntimeException(e);
        }

        return baos.toByteArray();

    }

    @Override
    public HttpHeaders getHttpHeaders(EAuxiliaryBookFormat format, EAuxiliaryBookType auxBookType) {
        HttpHeaders headers = new HttpHeaders();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String date = dateFormat.format(new Date());
        String fileName = auxBookType.name() + "_" + date;

        switch (format) {
            case PDF:
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + fileName + ".pdf");
                break;
            case EXCEL:
                headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.ms-excel");
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + fileName + ".xls");
                break;
            default:
                throw new IllegalArgumentException("Unsupported format: " + format);
        }
        return headers;
    }
}
