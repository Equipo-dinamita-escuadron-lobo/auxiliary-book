package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.mapper;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.request.GenerateAuxiliaryBookRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IAuxiliaryBookRestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "format", ignore = true)
    @Mapping(target = "template", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(source = "enterprise", target = "enterprise")
    @Mapping(source = "criteria", target = "criteria")
    AuxiliaryBook toDomain(GenerateAuxiliaryBookRequest generateAuxiliaryBookRequest);
}
