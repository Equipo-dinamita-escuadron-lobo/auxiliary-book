package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.unicauca.edu.co.auxiliary_book.domain.models.log.AuxiliaryBookLog;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookLogEntity;

@Mapper(componentModel = "spring", uses = { IAuxiliaryBookCommandEntityMapper.class })
public interface IAuxiliaryBookLogCommandEntityMapper {

    @Mapping(target = "createdAt", ignore = true)
    AuxiliaryBookLogEntity toEntity(AuxiliaryBookLog auxiliaryBookLog);

    AuxiliaryBookLog toDomain(AuxiliaryBookLogEntity auxiliaryBookLogEntity);
}
