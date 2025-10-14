package com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook;

import com.unicauca.edu.co.auxiliary_book.application.ports.in.auxiliaryBook.IAuxiliaryBookCommandPort;
import com.unicauca.edu.co.auxiliary_book.application.ports.out.IAccountingInfoClient;
import com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils.AuxiliaryBookProcessor;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import com.unicauca.edu.co.auxiliary_book.domain.models.log.AuxiliaryBookLog;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBook.IAuxiliaryBookCommandRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookLog.IAuxiliaryBookLogCommandRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.IFormatterResultOutputPort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.IMessageServicePort;
import com.unicauca.edu.co.auxiliary_book.infrastructure.config.i18n.MessageKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuxiliaryBookCommandUC implements IAuxiliaryBookCommandPort {

    private final IAuxiliaryBookCommandRepositoryPort abCommandRepositoryPort;
    private final IAuxiliaryBookLogCommandRepositoryPort abLogCommandRepositoryPort;

    private final IAccountingInfoClient accountingInfoQueryPort;

    private final AuxiliaryBookProcessor auxiliaryBookProcessor;

    private final IFormatterResultOutputPort formatterResultOutputPort;
    private final IMessageServicePort messageServicePort;

    @Override
    public AuxiliaryBook registerAuxiliaryBook(AuxiliaryBook auxiliaryBook) {

        AuxiliaryBook abRegistered = abCommandRepositoryPort.registerAuxiliaryBook(auxiliaryBook);
        abLogCommandRepositoryPort.registerAuxiliaryBookLog(AuxiliaryBookLog.builder()
                .book(abRegistered)
                .logTypeEvent("REGISTERED AUX_BOOK")
                .build());
        return abRegistered;
    }

    @Override
    public List<?> genereteAuxiliaryBookInfo(AuxiliaryBook auxiliaryBook) {
        return this.auxiliaryBookProcessor.processAuxiliaryBookData(this.accountingInfoQueryPort, auxiliaryBook);
    }
}
