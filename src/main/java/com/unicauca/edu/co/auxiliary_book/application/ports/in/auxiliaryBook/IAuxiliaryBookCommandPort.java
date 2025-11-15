package com.unicauca.edu.co.auxiliary_book.application.ports.in.auxiliaryBook;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;

import java.util.List;

/**
 * @brief Input port for Auxiliary Book write operations
 *
 * Defines the contract for registering and generating information for auxiliary books
 * in the application layer.
 */
public interface IAuxiliaryBookCommandPort {
    /**
     * @brief Registers a new auxiliary book
     * @param auxiliaryBook Auxiliary book to register
     * @return Registered AuxiliaryBook with generated identifiers
     */
    AuxiliaryBook registerAuxiliaryBook(AuxiliaryBook auxiliaryBook);

    /**
     * @brief Generates information for an auxiliary book
     * @param auxiliaryBook Auxiliary book for which to generate information
     * @return List containing generated auxiliary book information
     */
    List<?> genereteAuxiliaryBookInfo(AuxiliaryBook auxiliaryBook);
}
