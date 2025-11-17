package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.repository;

import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IAuxiliaryBookLogRepository extends JpaRepository<AuxiliaryBookLogEntity, Long> {
    /**
     * Busca todos los logs de un libro, usando el PUBLIC ID del libro.
     * JPA navegará: log -> auxiliaryBook -> publicId
     *
     * @param bookPublicId El ID público del AuxiliaryBook
     * @return Lista de logs
     */
    List<AuxiliaryBookLogEntity> findByAuxiliaryBookPublicId(String bookPublicId);
}
