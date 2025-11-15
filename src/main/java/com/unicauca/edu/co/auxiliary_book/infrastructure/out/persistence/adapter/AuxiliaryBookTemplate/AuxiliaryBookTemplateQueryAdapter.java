package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.adapter.AuxiliaryBookTemplate;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.export.AuxiliaryBookTemplate;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookTemplate.IAuxiliaryBookTemplateQueryRepositoryPort;

import java.util.List;

/**
 * @brief Adapter for Auxiliary Book Template read operations.
 *
 * Implements the contract for querying auxiliary book template records
 * from the underlying data storage system.
 */
public class AuxiliaryBookTemplateQueryAdapter implements IAuxiliaryBookTemplateQueryRepositoryPort {
    /**
     * @brief Retrieves all auxiliary book template records.
     * @return List of AuxiliaryBookTemplate objects.
     */
    @Override
    public List<AuxiliaryBookTemplate> findAll() {
        return List.of();
    }
}
