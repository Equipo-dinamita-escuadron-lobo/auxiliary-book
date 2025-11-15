package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.mapper;

import com.unicauca.edu.co.auxiliary_book.domain.models.log.AuxiliaryBookLog;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookLogEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IAuxiliaryBookLogQueryEntityMapper {
    AuxiliaryBookLog toDomain(AuxiliaryBookLogEntity logEntity);
    List<AuxiliaryBookLog> toDomainList(List<AuxiliaryBookLogEntity> logsEntities);
}
