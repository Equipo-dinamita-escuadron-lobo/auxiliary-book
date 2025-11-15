package com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookHistory;

import com.unicauca.edu.co.auxiliary_book.domain.models.history.AuxiliaryBookHistory;

/**
 * @brief Output port for Auxiliary Book History write operations
 *
 * Defines the contract for persisting auxiliary book history records
 * in the underlying data storage system.
 */
public interface IAuxiliaryBookHistoryCommandRepositoryPort {
    /**
     * @brief Persists an auxiliary book history record
     * @param auxiliaryBookHistory Auxiliary book history record to save
     * @return Saved AuxiliaryBookHistory with generated identifiers
     */
    AuxiliaryBookHistory registerAuxiliaryBookHistory(AuxiliaryBookHistory auxiliaryBookHistory);

    /**
     * @brief Updates an existing auxiliary book history record.
     * @param auxiliaryBookHistory Auxiliary book history record to update.
     * @return The updated AuxiliaryBookHistory.
     */
    AuxiliaryBookHistory updateAuxiliaryBookHistory(AuxiliaryBookHistory auxiliaryBookHistory);
}
