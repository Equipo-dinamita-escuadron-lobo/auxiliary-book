package com.unicauca.edu.co.auxiliary_book.application.ports.in.export;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.export.ExportInfo;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookFormat;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookType;
import org.springframework.http.HttpHeaders;

/**
 * @brief Input port for exporting auxiliary book reports
 *
 * Defines the contract for exporting reports and obtaining HTTP headers
 * for auxiliary book exports in the application layer.
 */
public interface IExportReportPort {
    /**
     * @brief Exports an auxiliary book report
     * @param exportInfo Information required for exporting the report
     * @return Byte array containing the exported report data
     */
    byte[] exportReport(ExportInfo exportInfo);

    /**
     * @brief Retrieves HTTP headers for the exported report
     * @param format Format of the auxiliary book report
     * @param auxBookType Type of the auxiliary book
     * @return HttpHeaders configured for the export response
     */
    HttpHeaders getHttpHeaders(EAuxiliaryBookFormat format, EAuxiliaryBookType auxBookType);
}
