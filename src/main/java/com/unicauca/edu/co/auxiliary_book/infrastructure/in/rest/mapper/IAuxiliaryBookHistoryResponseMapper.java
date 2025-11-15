package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.mapper;

import com.unicauca.edu.co.auxiliary_book.domain.models.history.AuxiliaryBookHistory;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.response.AuxiliaryBookHistoryResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IAuxiliaryBookHistoryResponseMapper {
    AuxiliaryBookHistoryResponseDTO toDtoResponse(AuxiliaryBookHistory history);
}
