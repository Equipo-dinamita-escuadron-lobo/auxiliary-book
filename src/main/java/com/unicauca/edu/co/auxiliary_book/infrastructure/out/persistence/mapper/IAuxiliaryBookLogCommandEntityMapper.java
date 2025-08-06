package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.mapper;

import com.unicauca.edu.co.auxiliary_book.domain.models.log.AuxiliaryBookLog;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookLogEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IAuxiliaryBookLogCommandEntityMapper {
    AuxiliaryBookLogEntity toEntity(AuxiliaryBookLog auxiliaryBookLog);
    AuxiliaryBookLog toDomain(AuxiliaryBookLogEntity auxiliaryBookLogEntity);
}
