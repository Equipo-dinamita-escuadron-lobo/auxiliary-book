package com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookLog;

import java.util.List;

import com.unicauca.edu.co.auxiliary_book.domain.models.log.AuxiliaryBookLog;

/**
 * @brief Output port for Auxiliary Book Log read operations
 *
 * Defines the contract for querying auxiliary book log records
 * from the underlying data storage system.
 */
public interface IAuxiliaryBookLogQueryRepositoryPort {
    /**
     * @brief Retrieves a list of auxiliary book log records for a one report
     * @param auxiliaryBookId Identifier of the auxiliary book whose logs are to be queried
     * @return List of AuxiliaryBookLog records for the given auxiliary book
     */
    List<AuxiliaryBookLog> findAllByAuxiliaryBookPublicId(String auxiliaryBookId);
}
