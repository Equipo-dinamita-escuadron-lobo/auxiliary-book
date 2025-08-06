package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.repository;

import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuxiliaryBookTemplateRepository extends JpaRepository<AuxiliaryBookTemplateEntity, Long> {
}
