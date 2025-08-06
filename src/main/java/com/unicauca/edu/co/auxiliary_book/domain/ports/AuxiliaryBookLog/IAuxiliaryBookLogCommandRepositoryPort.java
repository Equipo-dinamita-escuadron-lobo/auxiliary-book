package com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookLog;

import com.unicauca.edu.co.auxiliary_book.domain.models.log.AuxiliaryBookLog;

public interface IAuxiliaryBookLogCommandRepositoryPort {
    /**
     * Guarda el registro de un libro auxiliar en el repositorio.
     *
     * @param auxiliaryBookLog el libro auxiliar a guardar
     * @return el objeto AuxiliaryBookLog guardado
     */
    AuxiliaryBookLog registerAuxiliaryBookLog(AuxiliaryBookLog auxiliaryBookLog);
}
