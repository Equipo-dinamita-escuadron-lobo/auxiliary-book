package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.mapper;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.export.ExportInfo;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.request.ExportAuxiliaryBookRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IExportRestMapper {
    ExportInfo toDomain(ExportAuxiliaryBookRequest exportAuxiliaryBookRequest);
}
