package com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookHistory;

import com.unicauca.edu.co.auxiliary_book.domain.models.history.AuxiliaryBookHistory;

public interface IAuxiliaryBookHistoryCommandRepositoryPort {
    /**
     * Guarda el registro de un libro auxiliar en el repositorio.
     *
     * @param auxiliaryBookHistory el libro auxiliar a guardar
     * @return el objeto AuxiliaryBookHistory guardado
     */
    AuxiliaryBookHistory registerAuxiliaryBookHistory(AuxiliaryBookHistory auxiliaryBookHistory);
}
