package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.repository;

import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuxiliaryBookRepository extends JpaRepository<AuxiliaryBookEntity, Long> {

}
