package com.unicauca.edu.co.auxiliary_book.application.useCase.log;

import com.unicauca.edu.co.auxiliary_book.application.ports.in.log.IAuxiliaryBookLogCommandPort;
import com.unicauca.edu.co.auxiliary_book.domain.models.log.AuxiliaryBookLog;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookLog.IAuxiliaryBookLogCommandRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuxiliaryBookLogCommandUC implements IAuxiliaryBookLogCommandPort {

    private final IAuxiliaryBookLogCommandRepositoryPort abLogCommandRepositoryPort;

    @Override
    public AuxiliaryBookLog registerAuxiliaryBookLog(AuxiliaryBookLog auxiliaryBookLog) {
        return this.abLogCommandRepositoryPort.registerAuxiliaryBookLog(auxiliaryBookLog);
    }
}
