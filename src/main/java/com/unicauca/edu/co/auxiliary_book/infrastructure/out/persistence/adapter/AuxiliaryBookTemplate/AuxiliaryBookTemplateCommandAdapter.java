package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.adapter.AuxiliaryBookTemplate;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.export.AuxiliaryBookTemplate;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookTemplate.IAuxiliaryBookTemplateCommandRepositoryPort;

/**
 * @brief Adapter for Auxiliary Book Template write operations.
 *
 * Implements the contract for persisting auxiliary book template records
 * in the underlying data storage system.
 */
public class AuxiliaryBookTemplateCommandAdapter implements IAuxiliaryBookTemplateCommandRepositoryPort {
    /**
     * @brief Persists an auxiliary book template record.
     * @param auxiliaryBookTemplate Auxiliary book template to save.
     * @return Saved AuxiliaryBookTemplate with generated identifiers.
     */
    @Override
    public AuxiliaryBookTemplate registerAuxiliaryBookTemplate(AuxiliaryBookTemplate auxiliaryBookTemplate) {
        return null;
    }
}
