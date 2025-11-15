package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.adapter.AuxiliaryBookHistory;

import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookHistoryEntity;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.mapper.IAuxiliaryBookHistoryCommandEntityMapper;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.repository.IAuxiliaryBookHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.unicauca.edu.co.auxiliary_book.domain.models.history.AuxiliaryBookHistory;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookHistory.IAuxiliaryBookHistoryQueryRepositoryPort;
import org.springframework.stereotype.Component;

/**
 * @brief Adapter for Auxiliary Book History read operations.
 *
 * Implements the contract for querying auxiliary book history records
 * from the underlying data storage system.
 */
@Component
@RequiredArgsConstructor
public class AuxiliaryBookHistoryQueryAdapter implements IAuxiliaryBookHistoryQueryRepositoryPort {


    private final IAuxiliaryBookHistoryRepository auxiliaryBookHistoryRepository;
    private final IAuxiliaryBookHistoryCommandEntityMapper auxiliaryBookHistoryMapper;

    /**
     * @brief Retrieves a page of auxiliary book history records for a specific entity.
     * @param entId Identifier of the entity whose histories are to be queried.
     * @param pageable Pagination information.
     * @return Page of AuxiliaryBookHistory records for the given entity.
     */
    @Override
    public Page<AuxiliaryBookHistory> findPageByEntId(String entId, Pageable pageable) {
        // 1. Llamar al repositorio de JPA
        Page<AuxiliaryBookHistoryEntity> entityPage = this.auxiliaryBookHistoryRepository.findByAuxiliaryBookEntId(entId, pageable);

        return entityPage.map(this.auxiliaryBookHistoryMapper::toDomain);
    }

    /**
     * @brief Busca el historial de un libro por el ID interno del libro.
     * @param bookId El ID (Long) interno del AuxiliaryBook.
     * @return El AuxiliaryBookHistory, o null si no se encuentra.
     */
    @Override
    public AuxiliaryBookHistory findByBookId(Long bookId) {
        // 1. Llamar al repositorio de JPA
        AuxiliaryBookHistoryEntity entity = this.auxiliaryBookHistoryRepository.findByAuxiliaryBookId(bookId);

        // 2. Manejar el caso nulo
        if (entity == null) {
            return null;
        }

        // 3. Mapear de Entidad a Dominio y retornar
        return this.auxiliaryBookHistoryMapper.toDomain(entity);
    }
}
