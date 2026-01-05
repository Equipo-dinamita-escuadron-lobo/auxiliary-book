package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.unicauca.edu.co.auxiliary_book.domain.models.log.AuxiliaryBookLog;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookLogEntity;

@Mapper(componentModel = "spring", uses = { IAuxiliaryBookCommandEntityMapper.class })
public interface IAuxiliaryBookLogQueryEntityMapper {
    AuxiliaryBookLog toDomain(AuxiliaryBookLogEntity logEntity);
    List<AuxiliaryBookLog> toDomainList(List<AuxiliaryBookLogEntity> logsEntities);
}
