package com.unicauca.edu.co.auxiliary_book.copy.infrastructure.out.persistence;

import com.unicauca.edu.co.auxiliary_book.copy.application.output.IAuxBookSourceRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookEntity;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.scheduledReport.ScheduledReportEntity;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.repository.IAuxiliaryBookRepository;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.repository.scheduledReport.IScheduledReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

/**
 * Adaptador de salida: lectura de AuxiliaryBook y ScheduledReport del tenant de origen.
 * ADR-40.
 */
@Component
@RequiredArgsConstructor
public class AuxBookSourceRepositoryAdapter implements IAuxBookSourceRepositoryPort {

    private final IAuxiliaryBookRepository auxBookRepository;
    private final IScheduledReportRepository scheduledReportRepository;

    @Override
    public List<AuxiliaryBookEntity> findAuxBooksForCopy(String entOrigen, Instant snapshotCorte) {
        return auxBookRepository.findAll().stream()
                .filter(b -> entOrigen.equals(b.getEntId()))
                .filter(b -> b.getCreatedAt() == null
                        || !b.getCreatedAt().toInstant(java.time.ZoneOffset.UTC).isAfter(snapshotCorte))
                .toList();
    }

    @Override
    public List<ScheduledReportEntity> findScheduledReportsForCopy(String entOrigen, Instant snapshotCorte) {
        return scheduledReportRepository.findAll().stream()
                .filter(r -> entOrigen.equals(r.getEntId()))
                .filter(r -> r.getCreatedAt() == null
                        || !r.getCreatedAt().toInstant(java.time.ZoneOffset.UTC).isAfter(snapshotCorte))
                .toList();
    }
}
