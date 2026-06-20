package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.mapper;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.request.GenerateAuxiliaryBookRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @brief Mapper REST entre la solicitud de generación y el dominio.
 *
 * Convierte un {@link GenerateAuxiliaryBookRequest} recibido por la
 * capa REST en una entidad de dominio {@link AuxiliaryBook},
 * ignorando los campos que se asignan posteriormente (id, publicId,
 * fecha de creación, formato y plantilla). Implementado por MapStruct.
 */
@Mapper(componentModel = "spring")
public interface IAuxiliaryBookRestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publicId", ignore=true)
    @Mapping(target = "format", ignore = true)
    @Mapping(target = "template", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(source = "entId", target = "entId")
    @Mapping(source = "criteria", target = "criteria")
    AuxiliaryBook toDomain(GenerateAuxiliaryBookRequest generateAuxiliaryBookRequest);
}
