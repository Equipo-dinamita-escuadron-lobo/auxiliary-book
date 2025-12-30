package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.mapper;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.export.ExportInfo;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.request.ExportAuxiliaryBookRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IExportRestMapper {
    @Mapping(source = "format", target = "format")
    @Mapping(source = "entName", target = "entName")
    @Mapping(source = "auxiliaryBook", target = "auxiliaryBook")
    @Mapping(source = "auxBookData", target = "auxBookData")
    @Mapping(source = "infoReportTemplate", target = "infoReportTemplate")
    ExportInfo toDomain(ExportAuxiliaryBookRequest exportAuxiliaryBookRequest);
}
