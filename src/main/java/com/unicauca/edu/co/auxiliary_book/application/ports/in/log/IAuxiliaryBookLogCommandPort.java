package com.unicauca.edu.co.auxiliary_book.application.ports.in.log;

import com.unicauca.edu.co.auxiliary_book.domain.models.log.AuxiliaryBookLog;

/**
 * @brief Input port for Auxiliary Book Log write operations
 *
 * Defines the contract for registering auxiliary book log records
 * in the application layer.
 */
public interface IAuxiliaryBookLogCommandPort {
    /**
     * @brief Registers a new auxiliary book log record
     * @param auxiliaryBookLog Auxiliary book log to register
     * @return Registered AuxiliaryBookLog with generated identifiers
     */
    AuxiliaryBookLog registerAuxiliaryBookLog(AuxiliaryBookLog auxiliaryBookLog);
}
