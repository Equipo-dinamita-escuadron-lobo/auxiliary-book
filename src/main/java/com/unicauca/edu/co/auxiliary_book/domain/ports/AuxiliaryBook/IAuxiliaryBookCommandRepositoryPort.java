package com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBook;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;

public interface IAuxiliaryBookCommandRepositoryPort {
    /**
     * Guarda el registro de un libro auxiliar en el repositorio.
     *
     * @param auxiliaryBook el libro auxiliar a guardar
     * @return el objeto AuxiliaryBook guardado
     */
    AuxiliaryBook registerAuxiliaryBook(AuxiliaryBook auxiliaryBook);
}
