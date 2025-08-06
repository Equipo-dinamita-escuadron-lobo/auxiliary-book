package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.adapter.AuxiliaryBookHistory;

import com.unicauca.edu.co.auxiliary_book.domain.models.history.AuxiliaryBookHistory;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookHistory.IAuxiliaryBookHistoryCommandRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookHistoryEntity;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.mapper.IAuxiliaryBookHistoryCommandEntityMapper;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.repository.IAuxiliaryBookHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuxiliaryBookHistoryCommandAdapter implements IAuxiliaryBookHistoryCommandRepositoryPort {

    private final IAuxiliaryBookHistoryCommandEntityMapper auxiliaryBookHistoryCommandEntityMapper;
    private final IAuxiliaryBookHistoryRepository auxiliaryBookHistoryRepository;

    @Override
    public AuxiliaryBookHistory registerAuxiliaryBookHistory(AuxiliaryBookHistory auxiliaryBookHistory) {
        AuxiliaryBookHistoryEntity abHistoryEntity = this.auxiliaryBookHistoryCommandEntityMapper.toEntity(auxiliaryBookHistory);
        return this.auxiliaryBookHistoryCommandEntityMapper.toDomain(this.auxiliaryBookHistoryRepository.save(abHistoryEntity));
    }
}
