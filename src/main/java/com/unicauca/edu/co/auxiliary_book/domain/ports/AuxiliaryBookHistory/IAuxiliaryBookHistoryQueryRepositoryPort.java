package com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookHistory;

import com.unicauca.edu.co.auxiliary_book.domain.models.history.AuxiliaryBookHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @brief Output port for Auxiliary Book History read operations
 *
 * Defines the contract for querying auxiliary book history records
 * from the underlying data storage system.
 */
public interface IAuxiliaryBookHistoryQueryRepositoryPort {
    /**
     * @brief Retrieves a page of auxiliary book history records for a specific entity
     * @param entId Identifier of the entity whose histories are to be queried
     * @param pageable Pagination information
     * @return Page of AuxiliaryBookHistory records for the given entity
     */
    Page<AuxiliaryBookHistory> findPageByEntId(String entId, Pageable pageable);

    /**
     * @brief Busca el historial de un libro por el ID interno del libro.
     * @param bookId El ID (Long) interno del AuxiliaryBook.
     * @return El AuxiliaryBookHistory, o null si no se encuentra.
     */
    AuxiliaryBookHistory findByBookId(Long bookId);
}
