package com.unicauca.edu.co.auxiliary_book.application.ports.in.history;

import com.unicauca.edu.co.auxiliary_book.domain.models.history.AuxiliaryBookHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAuxiliaryBookHistoryQueryPort {
    Page<AuxiliaryBookHistory> findAllByEntId(String entId, Pageable pageable);
}
