package com.unicauca.edu.co.auxiliary_book.application.ports.in.history;

import com.unicauca.edu.co.auxiliary_book.domain.models.history.AuxiliaryBookHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @brief Input port for Auxiliary Book History query operations
 *
 * Defines the contract for querying auxiliary book history records
 * in the application layer.
 */
public interface IAuxiliaryBookHistoryQueryPort {
    /**
     * @brief Retrieves a page of auxiliary book history records for a specific entity
     * @param entId Identifier of the entity whose histories are to be queried
     * @param pageable Pagination information
     * @return Page of AuxiliaryBookHistory records for the given entity
     */
    Page<AuxiliaryBookHistory> findAllByEntId(String entId, Pageable pageable);
}
