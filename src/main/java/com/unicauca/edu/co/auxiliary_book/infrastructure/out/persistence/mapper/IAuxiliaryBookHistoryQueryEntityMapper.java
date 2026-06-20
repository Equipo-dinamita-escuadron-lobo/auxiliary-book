package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.unicauca.edu.co.auxiliary_book.domain.models.history.AuxiliaryBookHistory;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookHistoryEntity;

/**
 * @brief Mapper MapStruct para convertir entidades de historial a dominio (consultas).
 *
 * Convierte {@link AuxiliaryBookHistoryEntity} individual o en lista a
 * su modelo de dominio {@link AuxiliaryBookHistory}, reutilizando el
 * mapper del libro auxiliar para la asociación anidada.
 */
@Mapper(componentModel = "spring", uses = { IAuxiliaryBookCommandEntityMapper.class })
public interface IAuxiliaryBookHistoryQueryEntityMapper {
    AuxiliaryBookHistory toDomain(AuxiliaryBookHistoryEntity historyEntity);
    List<AuxiliaryBookHistory> toDomainList(List<AuxiliaryBookHistoryEntity> historyEntities);
}
