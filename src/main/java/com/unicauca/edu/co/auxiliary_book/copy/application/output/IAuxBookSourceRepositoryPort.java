package com.unicauca.edu.co.auxiliary_book.copy.application.output;

import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookEntity;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.scheduledReport.ScheduledReportEntity;

import java.time.Instant;
import java.util.List;

/**
 * Puerto de salida: lectura de auxiliary books y scheduled reports del tenant de origen.
 * ADR-40.
 */
public interface IAuxBookSourceRepositoryPort {

    List<AuxiliaryBookEntity> findAuxBooksForCopy(String entOrigen, Instant snapshotCorte);

    List<ScheduledReportEntity> findScheduledReportsForCopy(String entOrigen, Instant snapshotCorte);
}
