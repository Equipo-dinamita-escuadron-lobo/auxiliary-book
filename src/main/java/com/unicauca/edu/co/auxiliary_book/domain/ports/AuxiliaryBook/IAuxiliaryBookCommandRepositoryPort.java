package com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBook;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;

/**
 * @brief Output port for Auxiliary Book write operations
 *
 * Defines the contract for persisting auxiliary book records
 * in the underlying data storage system.
 */
public interface IAuxiliaryBookCommandRepositoryPort {
    /**
     * @brief Persists a new auxiliary book record
     * @param auxiliaryBook Auxiliary book to save
     * @return Saved auxiliary book with generated identifiers
     */
    AuxiliaryBook registerAuxiliaryBook(AuxiliaryBook auxiliaryBook);
}
