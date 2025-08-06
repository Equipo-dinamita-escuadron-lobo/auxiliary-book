package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.repository;

import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuxiliaryBookHistoryRepository extends JpaRepository<AuxiliaryBookHistoryEntity, Long> {
}
