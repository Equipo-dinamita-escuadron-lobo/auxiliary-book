package com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookTemplate;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.export.AuxiliaryBookTemplate;

import java.util.List;

/**
 * @brief Output port for Auxiliary Book Template read operations
 *
 * Defines the contract for querying auxiliary book template records
 * from the underlying data storage system.
 */
public interface IAuxiliaryBookTemplateQueryRepositoryPort {
    /**
     * @brief Retrieves all auxiliary book template records
     * @return List of AuxiliaryBookTemplate objects
     */
    List<AuxiliaryBookTemplate> findAll();
}
