package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.adapter.AuxiliaryBookLog;

import com.unicauca.edu.co.auxiliary_book.domain.models.log.AuxiliaryBookLog;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookLog.IAuxiliaryBookLogCommandRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookLogEntity;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.mapper.IAuxiliaryBookLogCommandEntityMapper;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.repository.IAuxiliaryBookLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @brief Adapter for Auxiliary Book Log write operations.
 *
 * Implements the contract for persisting auxiliary book log records
 * in the underlying data storage system.
 */
@Component
@RequiredArgsConstructor
public class AuxiliaryBookLogCommandAdapter implements IAuxiliaryBookLogCommandRepositoryPort {

    private final IAuxiliaryBookLogCommandEntityMapper auxiliaryBookLogEntityMapper;
    private final IAuxiliaryBookLogRepository auxiliaryBookLogRepository;

    /**
     * @brief Persists an auxiliary book log record.
     * @param auxiliaryBookLog Auxiliary book log to save.
     * @return Saved AuxiliaryBookLog with generated identifiers.
     */
    @Override
    public AuxiliaryBookLog registerAuxiliaryBookLog(AuxiliaryBookLog auxiliaryBookLog) {
        AuxiliaryBookLogEntity abLogEntity = this.auxiliaryBookLogEntityMapper.toEntity(auxiliaryBookLog);
        return this.auxiliaryBookLogEntityMapper.toDomain(this.auxiliaryBookLogRepository.save(abLogEntity));
    }
}
