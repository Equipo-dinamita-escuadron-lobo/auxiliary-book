package com.unicauca.edu.co.auxiliary_book.application.useCase.scheduledReport.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicauca.edu.co.auxiliary_book.application.ports.in.auxiliaryBook.IAuxiliaryBookCommandPort;
import com.unicauca.edu.co.auxiliary_book.application.ports.in.export.IExportReportPort;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.export.AuxiliaryBookTemplate;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.export.ExportInfo;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAlignment;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookFormat;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduling.ScheduledAuxiliaryBookJob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @brief Servicio reutilizable para generar el reporte (bytes) a partir de un job programado.
 *
 * Centraliza la creacion del AuxiliaryBook, la obtencion de datos contables
 * y la exportacion al formato solicitado, de modo que pueda ser invocado
 * tanto desde el scheduler (cuando el canal incluye email) como desde el
 * endpoint de descarga on-demand.
 *
 * Los DTOs tipados que devuelve {@code genereteAuxiliaryBookInfo} son
 * convertidos a {@link LinkedHashMap} via Jackson antes de ser pasados al
 * exportador, porque el {@code ReportDataBuilder} de exportacion espera
 * el mismo formato que el flujo HTTP inmediato (donde la data round-trips
 * por JSON FE-BE y llega como mapas genericos).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuxiliaryBookReportGenerator {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final IAuxiliaryBookCommandPort auxiliaryBookCommandPort;
    private final IExportReportPort exportReportPort;

    public GenerationResult generate(ScheduledAuxiliaryBookJob job, EAuxiliaryBookFormat format) {
        EAuxiliaryBookFormat resolvedFormat = format != null ? format : EAuxiliaryBookFormat.PDF;

        log.info("[ReportGenerator] start publicId={} bookType={} entId={} format={} criteria={}",
                job.getPublicId(), job.getBookType(), job.getEntId(), resolvedFormat, job.getCriteria());

        AuxiliaryBook book = AuxiliaryBook.builder()
                .type(job.getBookType())
                .entId(job.getEntId())
                .userId(job.getUserId())
                .format(resolvedFormat)
                .criteria(job.getCriteria())
                .build();

        AuxiliaryBook registeredBook = auxiliaryBookCommandPort.registerAuxiliaryBook(book);
        List<?> reportData = auxiliaryBookCommandPort.genereteAuxiliaryBookInfo(registeredBook);

        int rows = reportData != null ? reportData.size() : -1;
        log.info("[ReportGenerator] data rows={} for publicId={}", rows, job.getPublicId());

        List<?> serializableData = toLinkedHashMaps(reportData);

        ExportInfo exportInfo = new ExportInfo(
                resolvedFormat,
                resolveEntName(job),
                registeredBook,
                serializableData,
                buildDefaultTemplate(job)
        );

        byte[] bytes = exportReportPort.exportReport(exportInfo);
        if (bytes == null || bytes.length == 0) {
            throw new IllegalStateException("Exported report content is empty.");
        }
        log.info("[ReportGenerator] exported bytes={} for publicId={}", bytes.length, job.getPublicId());
        return new GenerationResult(registeredBook, reportData, bytes, resolvedFormat);
    }

    /**
     * Convierte cada DTO en un {@link LinkedHashMap} via Jackson, replicando
     * el formato que el {@code ReportDataBuilder} espera (idéntico al que
     * llega al endpoint inmediato cuando el FE reenvia la data en JSON).
     */
    private List<LinkedHashMap<String, Object>> toLinkedHashMaps(List<?> data) {
        if (data == null || data.isEmpty()) {
            return List.of();
        }
        return data.stream()
                .map(item -> {
                    @SuppressWarnings("unchecked")
                    LinkedHashMap<String, Object> map = OBJECT_MAPPER.convertValue(item, LinkedHashMap.class);
                    return map;
                })
                .toList();
    }

    private String resolveEntName(ScheduledAuxiliaryBookJob job) {
        if (job != null && job.getBookType() != null) {
            return job.getBookType().name();
        }
        return job != null ? job.getEntId() : null;
    }

    private AuxiliaryBookTemplate buildDefaultTemplate(ScheduledAuxiliaryBookJob job) {
        String templateName = job != null && job.getBookType() != null
                ? job.getBookType().name()
                : "Auxiliary Book";

        return AuxiliaryBookTemplate.builder()
                .id(0L)
                .name(templateName)
                .pathLogotype(null)
                .alienation(EAlignment.CENTER)
                .font("Arial")
                .fontSize(12)
                .mainColor("#0B3C61")
                .build();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GenerationResult {
        private AuxiliaryBook registeredBook;
        private List<?> reportData;
        private byte[] reportBytes;
        private EAuxiliaryBookFormat format;
    }
}
