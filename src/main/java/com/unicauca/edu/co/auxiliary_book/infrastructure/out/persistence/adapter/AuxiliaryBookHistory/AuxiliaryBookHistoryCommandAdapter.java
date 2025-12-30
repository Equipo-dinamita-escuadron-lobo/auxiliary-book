package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.adapter.AuxiliaryBookHistory;

import com.unicauca.edu.co.auxiliary_book.domain.models.history.AuxiliaryBookHistory;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookHistory.IAuxiliaryBookHistoryCommandRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookEntity;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookHistoryEntity;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.mapper.IAuxiliaryBookHistoryCommandEntityMapper;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.repository.IAuxiliaryBookHistoryRepository;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.repository.IAuxiliaryBookRepository;
import org.springframework.transaction.annotation.Transactional; // <-- Import corregido
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @brief Adapter for Auxiliary Book History write operations.
 *
 * Implements the contract for persisting auxiliary book history records
 * in the underlying data storage system.
 */
@Component
@RequiredArgsConstructor
public class AuxiliaryBookHistoryCommandAdapter implements IAuxiliaryBookHistoryCommandRepositoryPort {

    private final IAuxiliaryBookHistoryCommandEntityMapper auxiliaryBookHistoryCommandEntityMapper;
    private final IAuxiliaryBookHistoryRepository auxiliaryBookHistoryRepository;
    private final IAuxiliaryBookRepository auxiliaryBookRepository;

    /**
     * @brief Persists an auxiliary book history record.
     * @param auxiliaryBookHistory Auxiliary book history to save.
     * @return Saved AuxiliaryBookHistory with generated identifiers.
     */
    @Override
    @Transactional
    public AuxiliaryBookHistory registerAuxiliaryBookHistory(AuxiliaryBookHistory auxiliaryBookHistory) {
        AuxiliaryBookHistoryEntity abHistoryEntity = this.auxiliaryBookHistoryCommandEntityMapper.toEntity(auxiliaryBookHistory);

        // --- CORRECCIÓN AL TransientObjectException ---
        // Cambiamos getReferenceById (perezoso) por findById (activo).
        // Esto trae la entidad 'Book' real a la sesión de Hibernate.
        if (abHistoryEntity.getAuxiliaryBook() != null && abHistoryEntity.getAuxiliaryBook().getId() != null) {

            AuxiliaryBookEntity managedBook = this.auxiliaryBookRepository.findById(
                    abHistoryEntity.getAuxiliaryBook().getId()
            ).orElseThrow(() -> new jakarta.persistence.EntityNotFoundException(
                    "AuxiliaryBook not found with id: " + abHistoryEntity.getAuxiliaryBook().getId()
            ));

            abHistoryEntity.setAuxiliaryBook(managedBook);
        }

        AuxiliaryBookHistoryEntity savedEntity = this.auxiliaryBookHistoryRepository.save(abHistoryEntity);
        return this.auxiliaryBookHistoryCommandEntityMapper.toDomain(savedEntity);
    }

    /**
     * @brief Updates an existing auxiliary book history record.
     * @param auxiliaryBookHistory The history record (with ID) to update.
     */
    @Override
    @Transactional
    // --- CORRECCIÓN DE NOMBRE ---
    // El método debe llamarse 'updateHistory' para coincidir con el puerto
    public AuxiliaryBookHistory updateAuxiliaryBookHistory(AuxiliaryBookHistory auxiliaryBookHistory) {
        AuxiliaryBookHistoryEntity abHistoryEntity = this.auxiliaryBookHistoryCommandEntityMapper.toEntity(auxiliaryBookHistory);

        // --- Aplicar la misma lógica de findById aquí ---
        if (abHistoryEntity.getAuxiliaryBook() != null && abHistoryEntity.getAuxiliaryBook().getId() != null) {

            AuxiliaryBookEntity managedBook = this.auxiliaryBookRepository.findById(
                    abHistoryEntity.getAuxiliaryBook().getId()
            ).orElseThrow(() -> new jakarta.persistence.EntityNotFoundException(
                    "AuxiliaryBook not found with id: " + abHistoryEntity.getAuxiliaryBook().getId()
            ));
            abHistoryEntity.setAuxiliaryBook(managedBook);
        }

        AuxiliaryBookHistoryEntity savedEntity = this.auxiliaryBookHistoryRepository.save(abHistoryEntity);

        return this.auxiliaryBookHistoryCommandEntityMapper.toDomain(savedEntity);
    }
}