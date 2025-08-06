package com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook;

import com.unicauca.edu.co.auxiliary_book.application.ports.in.auxiliaryBook.IAuxiliaryBookCommandPort;
import com.unicauca.edu.co.auxiliary_book.application.ports.out.IAccountingInfoQueryPort;
import com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils.AuxiliaryBookCriteriaProcessor;
import com.unicauca.edu.co.auxiliary_book.domain.models.external.AccountingInfo;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import com.unicauca.edu.co.auxiliary_book.domain.models.history.AuxiliaryBookHistory;
import com.unicauca.edu.co.auxiliary_book.domain.models.log.AuxiliaryBookLog;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBook.IAuxiliaryBookCommandRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookHistory.IAuxiliaryBookHistoryCommandRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookLog.IAuxiliaryBookLogCommandRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.IFormatterResultOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuxiliaryBookCommandUC implements IAuxiliaryBookCommandPort {

    private final IAuxiliaryBookCommandRepositoryPort abCommandRepositoryPort;
    private final IAuxiliaryBookLogCommandRepositoryPort abLogCommandRepositoryPort;

    private final IAccountingInfoQueryPort accountingInfoQueryPort;

    private final IFormatterResultOutputPort formatterResultOutputPort;

    @Override
    public AuxiliaryBook registerAuxiliaryBook(AuxiliaryBook auxiliaryBook) {
        if (auxiliaryBook == null) {
            formatterResultOutputPort.returnResponseError(400, "The AuxiliaryBook cannot be null");
            return null;
        }

        try {
            AuxiliaryBook abRegistered = abCommandRepositoryPort.registerAuxiliaryBook(auxiliaryBook);

            AuxiliaryBookLog abRegisteredLog = AuxiliaryBookLog.builder()
                    .book(abRegistered)
                    .logTypeEvent("REGISTERED AUX_BOOK")
                    .build();

            abLogCommandRepositoryPort.registerAuxiliaryBookLog(abRegisteredLog);

            return abRegistered;

        } catch (Exception ex) {
            this.abLogCommandRepositoryPort.registerAuxiliaryBookLog(AuxiliaryBookLog.builder().book(auxiliaryBook).logTypeEvent("ERROR REGISTERING AUX_BOOK").build());
            formatterResultOutputPort.returnResponseError(500, "An unexpected error occurred while registering the Auxiliary Book.");
            return null;
        }
    }

    @Override
    public List<?> genereteAuxiliaryBookInfo(AuxiliaryBook auxiliaryBook) {
        AuxiliaryBookCriteriaProcessor objCriteriaProcessor = new AuxiliaryBookCriteriaProcessor();

        return objCriteriaProcessor.process(this.accountingInfoQueryPort, auxiliaryBook);
    }


}
