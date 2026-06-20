package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.mapper;

import com.unicauca.edu.co.auxiliary_book.domain.models.history.AuxiliaryBookHistory;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookHistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @brief Mapper MapStruct entre el historial de dominio y su entidad JPA.
 *
 * Convierte bidireccionalmente entre {@link AuxiliaryBookHistory} y
 * {@link AuxiliaryBookHistoryEntity}, delegando el mapeo del libro
 * auxiliar asociado al {@link IAuxiliaryBookCommandEntityMapper}.
 */
@Mapper(
        componentModel = "spring",
        uses = { IAuxiliaryBookCommandEntityMapper.class }
)
public interface IAuxiliaryBookHistoryCommandEntityMapper {
    @Mapping(source = "auxiliaryBook", target = "auxiliaryBook")
    AuxiliaryBookHistoryEntity toEntity(AuxiliaryBookHistory auxiliaryBookHistory);

    @Mapping(source = "auxiliaryBook", target = "auxiliaryBook")
    AuxiliaryBookHistory toDomain(AuxiliaryBookHistoryEntity auxiliaryBookHistoryEntity);
}
