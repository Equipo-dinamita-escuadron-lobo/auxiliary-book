package com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookTemplate;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.export.AuxiliaryBookTemplate;

/**
 * @brief Output port for Auxiliary Book Template write operations
 *
 * Defines the contract for persisting auxiliary book template records
 * in the underlying data storage system.
 */
public interface IAuxiliaryBookTemplateCommandRepositoryPort {
    /**
     * @brief Persists an auxiliary book template record
     * @param auxiliaryBookTemplate Auxiliary book template to save
     * @return Saved AuxiliaryBookTemplate with generated identifiers
     */
    AuxiliaryBookTemplate registerAuxiliaryBookTemplate(AuxiliaryBookTemplate auxiliaryBookTemplate);
}
