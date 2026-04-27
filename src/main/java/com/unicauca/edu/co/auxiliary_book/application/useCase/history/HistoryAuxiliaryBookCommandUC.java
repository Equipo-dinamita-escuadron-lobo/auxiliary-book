package com.unicauca.edu.co.auxiliary_book.application.useCase.history;

import com.unicauca.edu.co.auxiliary_book.application.ports.in.history.IAuxiliaryBookHistoryCommandPort;
import com.unicauca.edu.co.auxiliary_book.domain.models.history.AuxiliaryBookHistory;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookHistory.IAuxiliaryBookHistoryCommandRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @brief Caso de uso de escritura para el historial de Libros Auxiliares.
 *
 * Implementa el puerto de entrada {@link IAuxiliaryBookHistoryCommandPort}
 * y delega en el repositorio el registro y la actualización de los
 * registros históricos asociados al ciclo de vida de cada libro auxiliar.
 */
@Service
@RequiredArgsConstructor
public class HistoryAuxiliaryBookCommandUC implements IAuxiliaryBookHistoryCommandPort {

    private final IAuxiliaryBookHistoryCommandRepositoryPort abHistoryCommandRepositoryPort;

    @Override
    public AuxiliaryBookHistory registerAuxiliaryBookHistory(AuxiliaryBookHistory auxiliaryBookHistory) {
        return abHistoryCommandRepositoryPort.registerAuxiliaryBookHistory(auxiliaryBookHistory);
    }

    @Override
    public AuxiliaryBookHistory updateAuxiliaryBookHistory(AuxiliaryBookHistory auxiliaryBookHistory) {
        return this.abHistoryCommandRepositoryPort.updateAuxiliaryBookHistory(auxiliaryBookHistory);
    }
}
