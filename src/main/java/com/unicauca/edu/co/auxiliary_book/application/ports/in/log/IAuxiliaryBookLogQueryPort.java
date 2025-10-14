package com.unicauca.edu.co.auxiliary_book.application.ports.in.log;

import com.unicauca.edu.co.auxiliary_book.domain.models.log.AuxiliaryBookLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @brief Input port for Auxiliary Book Log query operations
 *
 * Defines the contract for querying auxiliary book log records
 * in the application layer.
 */
public interface IAuxiliaryBookLogQueryPort {
    /**
     * @brief Retrieves a page of auxiliary book log records for a specific entity
     * @param entId Identifier of the entity whose logs are to be queried
     * @param pageable Pagination information
     * @return Page of AuxiliaryBookLog records for the given entity
     */
    Page<AuxiliaryBookLog> findAllByEntId(String entId, Pageable pageable);
}
