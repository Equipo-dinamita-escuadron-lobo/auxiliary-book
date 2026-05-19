package com.unicauca.edu.co.auxiliary_book.copy.infrastructure.out.persistence;

import com.unicauca.edu.co.auxiliary_book.copy.application.output.IAuxBookCopyJobLogRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.copy.domain.models.CopyJobLog;
import com.unicauca.edu.co.auxiliary_book.copy.infrastructure.out.persistence.jpa.AuxBookCopyJobLogEntity;
import com.unicauca.edu.co.auxiliary_book.copy.infrastructure.out.persistence.jpa.AuxBookCopyJobLogJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador de salida: persiste el log de idempotencia de copia de auxiliary-book.
 * ADR-38.
 */
@Component
@RequiredArgsConstructor
public class AuxBookCopyJobLogRepositoryAdapter implements IAuxBookCopyJobLogRepositoryPort {

    private final AuxBookCopyJobLogJpaRepository jpaRepository;

    @Override
    public CopyJobLog guardar(CopyJobLog log) {
        AuxBookCopyJobLogEntity entity = toEntity(log);
        AuxBookCopyJobLogEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<CopyJobLog> buscarPorIdProcesoYFase(String idProceso, int fase) {
        return jpaRepository.findByIdProcesoAndFase(idProceso, fase)
                .map(this::toDomain);
    }

    @Override
    public Optional<CopyJobLog> buscarPorIdProceso(String idProceso) {
        return jpaRepository.findFirstByIdProcesoOrderByFaseDesc(idProceso)
                .map(this::toDomain);
    }

    @Override
    @Transactional
    public void eliminarPorIdProceso(String idProceso) {
        jpaRepository.deleteByIdProceso(idProceso);
    }

    private AuxBookCopyJobLogEntity toEntity(CopyJobLog log) {
        return AuxBookCopyJobLogEntity.builder()
                .idProceso(log.getIdProceso() != null ? log.getIdProceso().toString() : null)
                .fase(log.getFase())
                .modulo(log.getModulo())
                .estado(log.getEstado())
                .fechaInicio(log.getFechaInicio())
                .fechaFin(log.getFechaFin())
                .equivalenciasGeneradas(log.getEquivalenciasGeneradas() != null ? log.getEquivalenciasGeneradas() : 0)
                .errorMessage(log.getErrorMessage())
                .build();
    }

    private CopyJobLog toDomain(AuxBookCopyJobLogEntity entity) {
        return CopyJobLog.builder()
                .idProceso(entity.getIdProceso() != null ? UUID.fromString(entity.getIdProceso()) : null)
                .fase(entity.getFase())
                .modulo(entity.getModulo())
                .estado(entity.getEstado())
                .fechaInicio(entity.getFechaInicio())
                .fechaFin(entity.getFechaFin())
                .equivalenciasGeneradas(entity.getEquivalenciasGeneradas() != null ? entity.getEquivalenciasGeneradas() : 0)
                .errorMessage(entity.getErrorMessage())
                .build();
    }
}
