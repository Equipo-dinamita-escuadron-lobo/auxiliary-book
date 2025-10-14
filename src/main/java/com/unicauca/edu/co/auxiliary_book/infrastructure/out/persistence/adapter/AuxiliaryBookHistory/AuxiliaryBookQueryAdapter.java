package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.adapter.AuxiliaryBookHistory;

import com.unicauca.edu.co.auxiliary_book.domain.models.history.AuxiliaryBookHistory;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookHistory.IAuxiliaryBookHistoryQueryRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @brief Adapter for Auxiliary Book History read operations.
 *
 * Implements the contract for querying auxiliary book history records
 * from the underlying data storage system.
 */
public class AuxiliaryBookQueryAdapter implements IAuxiliaryBookHistoryQueryRepositoryPort {
    /**
     * @brief Retrieves a page of auxiliary book history records for a specific entity.
     * @param entId Identifier of the entity whose histories are to be queried.
     * @param pageable Pagination information.
     * @return Page of AuxiliaryBookHistory records for the given entity.
     */
    @Override
    public Page<AuxiliaryBookHistory> findAllByEntId(String entId, Pageable pageable) {
        return null;
    }
}
