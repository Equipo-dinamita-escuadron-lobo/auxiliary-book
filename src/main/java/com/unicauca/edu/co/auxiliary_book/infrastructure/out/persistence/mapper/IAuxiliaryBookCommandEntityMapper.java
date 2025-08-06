package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.mapper;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IAuxiliaryBookCommandEntityMapper {
    AuxiliaryBookEntity toEntity(AuxiliaryBook auxiliaryBook);
    AuxiliaryBook toDomain(AuxiliaryBookEntity auxiliaryBookEntity);
}
