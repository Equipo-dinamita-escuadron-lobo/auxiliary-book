package com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook;

import com.unicauca.edu.co.auxiliary_book.application.ports.in.auxiliaryBook.IAuxiliaryBookCommandPort;
import com.unicauca.edu.co.auxiliary_book.application.ports.out.IAccountingInfoClient;
import com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils.AuxiliaryBookProcessor;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import com.unicauca.edu.co.auxiliary_book.domain.models.log.AuxiliaryBookLog;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBook.IAuxiliaryBookCommandRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookLog.IAuxiliaryBookLogCommandRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.IFormatterResultOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuxiliaryBookCommandUC implements IAuxiliaryBookCommandPort {

    private final IAuxiliaryBookCommandRepositoryPort abCommandRepositoryPort;
    private final IAuxiliaryBookLogCommandRepositoryPort abLogCommandRepositoryPort;

    private final IAccountingInfoClient accountingInfoQueryPort;

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
            formatterResultOutputPort.returnResponseError(500, "An unexpected error occurred while registering the Auxiliary Book." +
                    "\nError: " + ex.getMessage());
            return null;
        }
    }

    @Override
    public List<?> genereteAuxiliaryBookInfo(AuxiliaryBook auxiliaryBook) {
        AuxiliaryBookProcessor objCriteriaProcessor = new AuxiliaryBookProcessor();
        return objCriteriaProcessor.proccessAuxiliaryBookData(this.accountingInfoQueryPort, auxiliaryBook);
    }
}
