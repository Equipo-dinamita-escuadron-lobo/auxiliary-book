package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.repository;

import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IAuxiliaryBookRepository extends JpaRepository<AuxiliaryBookEntity, Long> {
    /**
     * Busca una entidad por su ID público (UUID).
     * @param publicId El ID externo (UUID)
     * @return Optional con la entidad si se encuentra.
     */
    Optional<AuxiliaryBookEntity> findByPublicId(String publicId);
}
