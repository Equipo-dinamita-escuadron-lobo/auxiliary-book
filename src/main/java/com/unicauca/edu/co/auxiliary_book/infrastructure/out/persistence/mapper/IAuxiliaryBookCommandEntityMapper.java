package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.mapper;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IAuxiliaryBookCommandEntityMapper {

    @Mapping(source = "entId", target = "entId")
    @Mapping(source = "criteria.criteriaRange.fromRange", target = "criteria.fromRange")
    @Mapping(source = "criteria.criteriaRange.toRange", target = "criteria.toRange")
    @Mapping(source = "criteria.costCenterId", target = "criteria.costCenterId")
    @Mapping(source = "criteria.criteriaType", target = "criteria.criteriaType")
    @Mapping(source = "criteria.startDate", target = "criteria.startDate")
    @Mapping(source = "criteria.endDate", target = "criteria.endDate")
    @Mapping(source = "id", target = "id") // ✅ conservar el ID existente
    @Mapping(target = "log", ignore = true)
    @Mapping(target = "history", ignore = true)
    @Mapping(target = "criteria.auxiliaryBook", ignore = true)
    @Mapping(target = "template", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    AuxiliaryBookEntity toEntity(AuxiliaryBook auxiliaryBook);

    @Mapping(source = "entId", target = "entId")
    @Mapping(source = "criteria.fromRange", target = "criteria.criteriaRange.fromRange")
    @Mapping(source = "criteria.toRange", target = "criteria.criteriaRange.toRange")
    @Mapping(source = "criteria.costCenterId", target = "criteria.costCenterId")
    @Mapping(source = "criteria.criteriaType", target = "criteria.criteriaType")
    @Mapping(source = "criteria.startDate", target = "criteria.startDate")
    @Mapping(source = "criteria.endDate", target = "criteria.endDate")
    @Mapping(target = "template", ignore = true)
    @Mapping(target = "criteria.voucherType", ignore = true)
    AuxiliaryBook toDomain(AuxiliaryBookEntity auxiliaryBookEntity);
}



