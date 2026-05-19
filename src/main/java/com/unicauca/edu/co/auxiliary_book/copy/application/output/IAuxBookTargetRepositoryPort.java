package com.unicauca.edu.co.auxiliary_book.copy.application.output;

import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookEntity;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.scheduledReport.ScheduledReportEntity;

/**
 * Puerto de salida: escritura de auxiliary books y scheduled reports en el tenant de destino.
 * ADR-40.
 */
public interface IAuxBookTargetRepositoryPort {

    AuxiliaryBookEntity guardarAuxBook(AuxiliaryBookEntity book);

    ScheduledReportEntity guardarScheduledReport(ScheduledReportEntity report);
}
