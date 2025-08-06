package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.adapter.AuxiliaryBookTemplate;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBookTemplate;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookTemplate.IAuxiliaryBookTemplateQueryRepositoryPort;

import java.util.List;

public class AuxiliaryBookTemplateQueryAdapter implements IAuxiliaryBookTemplateQueryRepositoryPort {
    @Override
    public List<AuxiliaryBookTemplate> findAll() {
        return List.of();
    }
}
