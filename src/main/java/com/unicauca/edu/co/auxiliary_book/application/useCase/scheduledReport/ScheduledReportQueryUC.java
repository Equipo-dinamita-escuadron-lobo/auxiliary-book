package com.unicauca.edu.co.auxiliary_book.application.useCase.scheduledReport;

import com.unicauca.edu.co.auxiliary_book.application.ports.in.scheduledReport.IScheduledReportQueryPort;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduling.ScheduledAuxiliaryBookJob;
import com.unicauca.edu.co.auxiliary_book.domain.ports.IFormatterResultOutputPort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.IMessageServicePort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.scheduledReport.IScheduledReportQueryRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.infrastructure.config.i18n.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @brief Caso de uso de consulta para reportes programados.
 *
 * Implementa el puerto de entrada {@link IScheduledReportQueryPort} y
 * expone la consulta de jobs por entidad y la búsqueda de un job puntual
 * por su identificador público, validando entradas y respondiendo con
 * los errores apropiados cuando no existen.
 */
@Service
@RequiredArgsConstructor
public class ScheduledReportQueryUC implements IScheduledReportQueryPort {

    private final IScheduledReportQueryRepositoryPort scheduledReportQueryRepositoryPort;
    private final IFormatterResultOutputPort formatterResultOutputPort;
    private final IMessageServicePort messageServicePort;

    @Override
    public List<ScheduledAuxiliaryBookJob> listScheduledReports(String entId) {
        if (entId == null || entId.isBlank()) {
            this.formatterResultOutputPort.returnBusinessRuleErrorResponse(400,
                    this.messageServicePort.getMessage(MessageKeys.VALIDATION_FIELD_REQUIRED, "entId"));
        }
        return this.scheduledReportQueryRepositoryPort.findByEntId(entId);
    }

    @Override
    public ScheduledAuxiliaryBookJob getScheduledReport(String publicId) {
        if (publicId == null || publicId.isBlank()) {
            this.formatterResultOutputPort.returnBusinessRuleErrorResponse(400,
                    this.messageServicePort.getMessage(MessageKeys.VALIDATION_FIELD_REQUIRED, "publicId"));
        }

        return this.scheduledReportQueryRepositoryPort.findByPublicId(publicId)
                .orElseGet(() -> {
                    this.formatterResultOutputPort.returnEntityDoesNotExistErrorResponse(404,
                            this.messageServicePort.getMessage(MessageKeys.ERROR_NOT_FOUND, "ScheduledReport"));
                    return null;
                });
    }
}
