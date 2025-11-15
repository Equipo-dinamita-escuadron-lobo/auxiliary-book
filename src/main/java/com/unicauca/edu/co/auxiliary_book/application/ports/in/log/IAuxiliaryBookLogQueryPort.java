package com.unicauca.edu.co.auxiliary_book.application.ports.in.log;

import com.unicauca.edu.co.auxiliary_book.domain.models.log.AuxiliaryBookLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @brief Input port for Auxiliary Book Log query operations
 *
 * Defines the contract for querying auxiliary book log records
 * in the application layer.
 */
public interface IAuxiliaryBookLogQueryPort {
    /**
     * @brief Retrieves a list of auxiliary book log records for a one report
     * @param auxiliaryBookId Identifier of the auxiliary book whose logs are to be queried
     * @return List of AuxiliaryBookLog records for the given auxiliary book
     */
    List<AuxiliaryBookLog> findAllByAuxiliaryBookPublicId(String auxiliaryBookId);
}
