package com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookLog;

import com.unicauca.edu.co.auxiliary_book.domain.models.log.AuxiliaryBookLog;

/**
 * @brief Output port for Auxiliary Book Log write operations
 *
 * Defines the contract for persisting auxiliary book log records
 * in the underlying data storage system.
 */
public interface IAuxiliaryBookLogCommandRepositoryPort {
    /**
     * @brief Persists an auxiliary book log record
     * @param auxiliaryBookLog Auxiliary book log record to save
     * @return Saved AuxiliaryBookLog with generated identifiers
     */
    AuxiliaryBookLog registerAuxiliaryBookLog(AuxiliaryBookLog auxiliaryBookLog);
}
