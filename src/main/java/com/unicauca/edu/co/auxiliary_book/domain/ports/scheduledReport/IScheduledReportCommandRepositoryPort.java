package com.unicauca.edu.co.auxiliary_book.domain.ports.scheduledReport;

import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EJobStatus;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduling.ScheduledAuxiliaryBookJob;

/**
 * @brief Puerto de salida para operaciones de escritura de reportes programados.
 *
 * Define el contrato para guardar un job programado y para actualizar
 * su estado ({@link EJobStatus}) sin modificar el resto de campos.
 */
public interface IScheduledReportCommandRepositoryPort {
    ScheduledAuxiliaryBookJob save(ScheduledAuxiliaryBookJob job);
    void updateStatus(Long jobId, EJobStatus status);
}
