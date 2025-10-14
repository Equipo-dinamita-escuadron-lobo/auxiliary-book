package com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookLog;

import com.unicauca.edu.co.auxiliary_book.domain.models.log.AuxiliaryBookLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @brief Output port for Auxiliary Book Log read operations
 *
 * Defines the contract for querying auxiliary book log records
 * from the underlying data storage system.
 */
public interface IAuxiliaryBookLogQueryRepositoryPort {
    /**
     * @brief Retrieves a page of auxiliary book log records for a specific entity
     * @param entId Identifier of the entity whose logs are to be queried
     * @param pageable Pagination information
     * @return Page of AuxiliaryBookLog records for the given entity
     */
    Page<AuxiliaryBookLog> findAllByEntId(String entId, Pageable pageable);
}
