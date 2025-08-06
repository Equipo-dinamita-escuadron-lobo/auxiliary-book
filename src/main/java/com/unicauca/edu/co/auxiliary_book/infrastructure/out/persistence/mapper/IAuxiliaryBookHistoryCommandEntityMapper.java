package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.mapper;

import com.unicauca.edu.co.auxiliary_book.domain.models.history.AuxiliaryBookHistory;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookHistoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IAuxiliaryBookHistoryCommandEntityMapper {
    AuxiliaryBookHistoryEntity toEntity(AuxiliaryBookHistory auxiliaryBookHistory);
    AuxiliaryBookHistory toDomain(AuxiliaryBookHistoryEntity auxiliaryBookHistoryEntity);
}
