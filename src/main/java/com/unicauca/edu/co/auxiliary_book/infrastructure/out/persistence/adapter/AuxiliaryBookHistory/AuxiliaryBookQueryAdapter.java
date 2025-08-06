package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.adapter.AuxiliaryBookHistory;

import com.unicauca.edu.co.auxiliary_book.domain.models.history.AuxiliaryBookHistory;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookHistory.IAuxiliaryBookHistoryQueryRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class AuxiliaryBookQueryAdapter implements IAuxiliaryBookHistoryQueryRepositoryPort {
    @Override
    public Page<AuxiliaryBookHistory> findAllByEntId(String entId, Pageable pageable) {
        return null;
    }
}
