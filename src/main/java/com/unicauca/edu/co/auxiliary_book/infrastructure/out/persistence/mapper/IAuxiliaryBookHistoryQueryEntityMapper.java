package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.unicauca.edu.co.auxiliary_book.domain.models.history.AuxiliaryBookHistory;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookHistoryEntity;

@Mapper(componentModel = "spring", uses = { IAuxiliaryBookCommandEntityMapper.class })
public interface IAuxiliaryBookHistoryQueryEntityMapper {
    AuxiliaryBookHistory toDomain(AuxiliaryBookHistoryEntity historyEntity);
    List<AuxiliaryBookHistory> toDomainList(List<AuxiliaryBookHistoryEntity> historyEntities);
}
