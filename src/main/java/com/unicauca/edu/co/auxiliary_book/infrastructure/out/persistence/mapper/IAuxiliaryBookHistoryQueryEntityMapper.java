package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.mapper;

import com.unicauca.edu.co.auxiliary_book.domain.models.history.AuxiliaryBookHistory;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookHistoryEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IAuxiliaryBookHistoryQueryEntityMapper {
    AuxiliaryBookHistory toDomain(AuxiliaryBookHistoryEntity historyEntity);
    List<AuxiliaryBookHistory> toDomainList(List<AuxiliaryBookHistoryEntity> historyEntities);
}
