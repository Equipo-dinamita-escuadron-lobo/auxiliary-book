package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.adapter.AuxiliaryBookLog;

import com.unicauca.edu.co.auxiliary_book.domain.models.log.AuxiliaryBookLog;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookLog.IAuxiliaryBookLogQueryRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class AuxiliaryBookLogQueryAdapter implements IAuxiliaryBookLogQueryRepositoryPort {
    @Override
    public Page<AuxiliaryBookLog> findAllByEntId(String entId, Pageable pageable) {
        return null;
    }
}
