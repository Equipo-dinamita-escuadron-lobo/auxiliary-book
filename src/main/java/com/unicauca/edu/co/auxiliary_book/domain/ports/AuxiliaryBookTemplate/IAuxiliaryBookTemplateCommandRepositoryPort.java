package com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookTemplate;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBookTemplate;

public interface IAuxiliaryBookTemplateCommandRepositoryPort {
    /**
     * Guarda la plantilla de un libro auxiliar en el repositorio.
     *
     * @param auxiliaryBookTemplate la plantilla del libro auxiliar a guardar
     * @return la plantilla del libro auxiliar guardada
     */
    AuxiliaryBookTemplate registerAuxiliaryBookTemplate(AuxiliaryBookTemplate auxiliaryBookTemplate);
}
