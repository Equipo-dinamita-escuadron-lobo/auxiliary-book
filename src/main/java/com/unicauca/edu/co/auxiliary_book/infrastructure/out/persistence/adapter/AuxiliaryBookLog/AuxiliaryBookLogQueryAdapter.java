package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.adapter.AuxiliaryBookLog;

import com.unicauca.edu.co.auxiliary_book.domain.models.log.AuxiliaryBookLog;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookLog.IAuxiliaryBookLogQueryRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.mapper.IAuxiliaryBookLogQueryEntityMapper;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.repository.IAuxiliaryBookLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @brief Adapter for Auxiliary Book Log read operations.
 *
 * Implements the contract for querying auxiliary book log records
 * from the underlying data storage system.
 */
@Component
@RequiredArgsConstructor
public class AuxiliaryBookLogQueryAdapter implements IAuxiliaryBookLogQueryRepositoryPort {

    private final IAuxiliaryBookLogQueryEntityMapper abLogQueryEntityMapper;
    private final IAuxiliaryBookLogRepository abLogRepository;

    @Override
    public List<AuxiliaryBookLog> findAllByAuxiliaryBookPublicId(String auxiliaryBookId) {
        return this.abLogQueryEntityMapper.toDomainList(this.abLogRepository.findByAuxiliaryBookPublicId(auxiliaryBookId));
    }

}
