package com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookTemplate;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBookTemplate;

import java.util.List;

public interface IAuxiliaryBookTemplateQueryRepositoryPort {
    /**
     * Obtiene una lista de plantillas para le generacion de libros auxiliares.
     *
     * @return una lista de objetos AuxiliaryBookTemplate
     */
    List<AuxiliaryBookTemplate> findAll();
}
