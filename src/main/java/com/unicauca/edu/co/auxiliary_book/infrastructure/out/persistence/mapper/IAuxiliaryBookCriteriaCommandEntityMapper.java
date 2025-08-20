package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.mapper;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.AuxiliaryBookCriteria;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookCriteriaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IAuxiliaryBookCriteriaCommandEntityMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "criteriaType", target = "criteriaType")
    @Mapping(source = "criteriaRange.fromRange", target = "fromRange")
    @Mapping(source = "criteriaRange.toRange", target = "toRange")
    AuxiliaryBookCriteriaEntity toCriteriaEntity(AuxiliaryBookCriteria domain);
}
