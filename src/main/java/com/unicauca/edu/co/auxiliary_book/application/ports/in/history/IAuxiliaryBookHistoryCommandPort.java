package com.unicauca.edu.co.auxiliary_book.application.ports.in.history;

import com.unicauca.edu.co.auxiliary_book.domain.models.history.AuxiliaryBookHistory;

public interface IAuxiliaryBookHistoryCommandPort {
    AuxiliaryBookHistory registerAuxiliaryBookHistory(AuxiliaryBookHistory auxiliaryBookHistory);
}
