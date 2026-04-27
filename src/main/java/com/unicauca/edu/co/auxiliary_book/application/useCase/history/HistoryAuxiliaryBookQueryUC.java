package com.unicauca.edu.co.auxiliary_book.application.useCase.history;

import com.unicauca.edu.co.auxiliary_book.application.ports.in.history.IAuxiliaryBookHistoryQueryPort;
import com.unicauca.edu.co.auxiliary_book.domain.models.history.AuxiliaryBookHistory;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookHistory.IAuxiliaryBookHistoryQueryRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.IFormatterResultOutputPort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.IMessageServicePort;
import com.unicauca.edu.co.auxiliary_book.infrastructure.config.i18n.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @brief Caso de uso de consulta para el historial de Libros Auxiliares.
 *
 * Implementa el puerto de entrada {@link IAuxiliaryBookHistoryQueryPort}
 * y consulta de manera paginada los registros históricos por entidad,
 * validando los parámetros de entrada antes de delegar en el repositorio.
 */
@Service
@RequiredArgsConstructor
public class HistoryAuxiliaryBookQueryUC implements IAuxiliaryBookHistoryQueryPort {

    private final IAuxiliaryBookHistoryQueryRepositoryPort abHistoryQueryRepositoryPort;

    private final IFormatterResultOutputPort formatterResultOutputPort;
    private final IMessageServicePort messageServicePort;

    @Override
    public Page<AuxiliaryBookHistory> findPageByEntId(String entId, Pageable pageable) {
        if(entId.isEmpty()){
            this.formatterResultOutputPort.returnBusinessRuleErrorResponse(404, this.messageServicePort.getMessage(MessageKeys.VALIDATION_FIELD_REQUIRED));
        }
        return this.abHistoryQueryRepositoryPort.findPageByEntId(entId,pageable);
    }
}
