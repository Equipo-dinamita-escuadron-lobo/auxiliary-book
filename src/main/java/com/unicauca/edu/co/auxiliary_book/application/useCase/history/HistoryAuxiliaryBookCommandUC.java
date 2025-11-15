package com.unicauca.edu.co.auxiliary_book.application.useCase.history;

import com.unicauca.edu.co.auxiliary_book.application.ports.in.history.IAuxiliaryBookHistoryCommandPort;
import com.unicauca.edu.co.auxiliary_book.domain.models.history.AuxiliaryBookHistory;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookHistory.IAuxiliaryBookHistoryCommandRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookLog.IAuxiliaryBookLogCommandRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoryAuxiliaryBookCommandUC implements IAuxiliaryBookHistoryCommandPort {

    private final IAuxiliaryBookHistoryCommandRepositoryPort abHistoryCommandRepositoryPort;
    private final IAuxiliaryBookLogCommandRepositoryPort abLogCommandRepositoryPort;

    @Override
    public AuxiliaryBookHistory registerAuxiliaryBookHistory(AuxiliaryBookHistory auxiliaryBookHistory) {
        AuxiliaryBookHistory abHistoryRegistered = abHistoryCommandRepositoryPort.registerAuxiliaryBookHistory(auxiliaryBookHistory);
        abLogCommandRepositoryPort.registerAuxiliaryBookLog(com.unicauca.edu.co.auxiliary_book.domain.models.log.AuxiliaryBookLog.builder()
                .book(abHistoryRegistered.getBook())
                .logTypeEvent("CREATED AUX_BOOK_HISTORY")
                .build());
        return abHistoryRegistered;
    }
}
