package com.unicauca.edu.co.auxiliary_book.copy.infrastructure.out.persistence;

import com.unicauca.edu.co.auxiliary_book.copy.application.output.IAuxBookTargetRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookEntity;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.scheduledReport.ScheduledReportEntity;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.repository.IAuxiliaryBookRepository;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.repository.scheduledReport.IScheduledReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Adaptador de salida: escritura de AuxiliaryBook y ScheduledReport en el tenant de destino.
 * ADR-40.
 */
@Component
@RequiredArgsConstructor
public class AuxBookTargetRepositoryAdapter implements IAuxBookTargetRepositoryPort {

    private final IAuxiliaryBookRepository auxBookRepository;
    private final IScheduledReportRepository scheduledReportRepository;

    @Override
    public AuxiliaryBookEntity guardarAuxBook(AuxiliaryBookEntity book) {
        return auxBookRepository.save(book);
    }

    @Override
    public ScheduledReportEntity guardarScheduledReport(ScheduledReportEntity report) {
        return scheduledReportRepository.save(report);
    }
}
