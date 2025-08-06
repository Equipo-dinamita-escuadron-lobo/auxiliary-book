package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.repository;

import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuxiliaryBookLogRepository extends JpaRepository<AuxiliaryBookLogEntity, Long> {
}
