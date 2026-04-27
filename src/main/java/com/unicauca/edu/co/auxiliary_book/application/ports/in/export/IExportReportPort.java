package com.unicauca.edu.co.auxiliary_book.application.ports.in.export;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.export.ExportInfo;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookFormat;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookType;
import org.springframework.http.HttpHeaders;

/**
 * @brief Puerto de entrada para la exportación de libros auxiliares.
 *
 * Define el contrato, dentro de la capa de aplicación, para exportar
 * reportes de libros auxiliares a los distintos formatos soportados y
 * para construir las cabeceras HTTP asociadas a la descarga del archivo.
 */
public interface IExportReportPort {
    /**
     * @brief Exporta un reporte de libro auxiliar.
     * @param exportInfo Información necesaria para construir el reporte.
     * @return Arreglo de bytes con el reporte exportado en el formato solicitado.
     */
    byte[] exportReport(ExportInfo exportInfo);

    /**
     * @brief Obtiene las cabeceras HTTP de la respuesta de exportación.
     * @param format Formato de salida del libro auxiliar (PDF, XLSX, etc.).
     * @param auxBookType Tipo de libro auxiliar exportado.
     * @return HttpHeaders configuradas con Content-Type y Content-Disposition.
     */
    HttpHeaders getHttpHeaders(EAuxiliaryBookFormat format, EAuxiliaryBookType auxBookType);
}
