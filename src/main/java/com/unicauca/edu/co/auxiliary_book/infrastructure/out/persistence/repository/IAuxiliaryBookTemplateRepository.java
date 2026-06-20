package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.repository;

import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @brief Repositorio JPA para la entidad de plantilla del libro auxiliar.
 *
 * Provee las operaciones CRUD estándar de Spring Data sin consultas
 * personalizadas adicionales.
 */
public interface IAuxiliaryBookTemplateRepository extends JpaRepository<AuxiliaryBookTemplateEntity, Long> {
}
