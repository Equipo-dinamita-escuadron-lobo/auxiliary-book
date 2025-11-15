package com.unicauca.edu.co.auxiliary_book.application.ports.in.history;

import com.unicauca.edu.co.auxiliary_book.domain.models.history.AuxiliaryBookHistory;

/**
 * @brief Input port for Auxiliary Book History write operations
 *
 * Defines the contract for registering auxiliary book history records
 * in the application layer.
 */
public interface IAuxiliaryBookHistoryCommandPort {
    /**
     * @brief Registers a new auxiliary book history record
     * @param auxiliaryBookHistory Auxiliary book history to register
     * @return Registered AuxiliaryBookHistory with generated identifiers
     */
    AuxiliaryBookHistory registerAuxiliaryBookHistory(AuxiliaryBookHistory auxiliaryBookHistory);

    /**
     * @brief Registers a new auxiliary book history record
     * @param auxiliaryBookHistory Auxiliary book history to update
     * @return Registered AuxiliaryBookHistory with generated identifiers
     */
    AuxiliaryBookHistory updateAuxiliaryBookHistory(AuxiliaryBookHistory auxiliaryBookHistory);
}
