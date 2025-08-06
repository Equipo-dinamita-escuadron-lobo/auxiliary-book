package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.adapter.AuxiliaryBookLog;

import com.unicauca.edu.co.auxiliary_book.domain.models.log.AuxiliaryBookLog;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookLog.IAuxiliaryBookLogCommandRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookLogEntity;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.mapper.IAuxiliaryBookLogCommandEntityMapper;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.repository.IAuxiliaryBookLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuxiliaryBookLogCommandAdapter implements IAuxiliaryBookLogCommandRepositoryPort {

    private final IAuxiliaryBookLogCommandEntityMapper auxiliaryBookLogEntityMapper;
    private final IAuxiliaryBookLogRepository auxiliaryBookLogRepository;

    @Override
    public AuxiliaryBookLog registerAuxiliaryBookLog(AuxiliaryBookLog auxiliaryBookLog) {
        AuxiliaryBookLogEntity abLogEntity = this.auxiliaryBookLogEntityMapper.toEntity(auxiliaryBookLog);
        return this.auxiliaryBookLogEntityMapper.toDomain(this.auxiliaryBookLogRepository.save(abLogEntity));
    }
}
