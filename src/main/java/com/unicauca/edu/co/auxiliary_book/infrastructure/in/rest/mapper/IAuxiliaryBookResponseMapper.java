package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.mapper;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.response.InventoryAndBalancesBookResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IAuxiliaryBookResponseMapper {
    InventoryAndBalancesBookResponse toDTOResponse(AuxiliaryBook auxiliaryBook);
}
