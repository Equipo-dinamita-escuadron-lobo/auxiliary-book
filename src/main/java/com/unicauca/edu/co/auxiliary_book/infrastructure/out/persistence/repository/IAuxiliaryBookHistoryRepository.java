package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.repository;

import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IAuxiliaryBookHistoryRepository extends JpaRepository<AuxiliaryBookHistoryEntity, Long> {
    /**
     * Busca una entrada de historial por su ID público (UUID).
     */
    Optional<AuxiliaryBookHistoryEntity> findByPublicId(String publicId);

    /**
     * Busca todas las entradas de historial que pertenecen a un AuxiliaryBook
     * basándose en el 'entId' de dicho libro.
     *
     * @param entId El entId (String) del AuxiliaryBookEntity asociado.
     * @param pageable La información de paginación.
     * @return Una página de AuxiliaryBookHistoryEntity.
     */
    Page<AuxiliaryBookHistoryEntity> findByAuxiliaryBookEntId(String entId, Pageable pageable);

    /**
     * Busca la entrada de historial asociada a un AuxiliaryBook por su ID interno.
     * Asume una relación OneToOne (un libro solo tiene un historial "actual").
     *
     * @param auxiliaryBookId El ID (Long) interno del AuxiliaryBookEntity.
     * @return La entidad de historial, o null si no se encuentra.
     */
    AuxiliaryBookHistoryEntity findByAuxiliaryBookId(Long auxiliaryBookId);
}
