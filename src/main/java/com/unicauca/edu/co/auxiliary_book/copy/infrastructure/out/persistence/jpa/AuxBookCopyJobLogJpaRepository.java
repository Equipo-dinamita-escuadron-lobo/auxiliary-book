package com.unicauca.edu.co.auxiliary_book.copy.infrastructure.out.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio Spring Data JPA para el log de idempotencia de copia de auxiliary-book.
 * ADR-38.
 */
@Repository
public interface AuxBookCopyJobLogJpaRepository extends JpaRepository<AuxBookCopyJobLogEntity, Long> {

    Optional<AuxBookCopyJobLogEntity> findByIdProcesoAndFase(String idProceso, int fase);

    Optional<AuxBookCopyJobLogEntity> findFirstByIdProcesoOrderByFaseDesc(String idProceso);

    void deleteByIdProceso(String idProceso);
}
