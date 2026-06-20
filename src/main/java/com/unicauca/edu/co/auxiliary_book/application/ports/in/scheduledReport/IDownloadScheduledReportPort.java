package com.unicauca.edu.co.auxiliary_book.application.ports.in.scheduledReport;

import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @brief Puerto de entrada para descargar el archivo de una ejecucion programada
 *        (modo DOWNLOAD): regenera el reporte on-demand a partir del job.
 */
public interface IDownloadScheduledReportPort {

    DownloadedReport download(String publicId, UUID executionId);

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class DownloadedReport {
        private byte[] content;
        private String filename;
        private EAuxiliaryBookFormat format;
    }
}
