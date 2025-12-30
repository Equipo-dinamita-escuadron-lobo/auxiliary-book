package com.unicauca.edu.co.auxiliary_book.application.useCase.log;

import com.unicauca.edu.co.auxiliary_book.application.ports.in.log.IAuxiliaryBookLogQueryPort;
import com.unicauca.edu.co.auxiliary_book.domain.models.log.AuxiliaryBookLog;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookLog.IAuxiliaryBookLogQueryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuxiliaryBookLogQueryUC implements IAuxiliaryBookLogQueryPort {

    private final IAuxiliaryBookLogQueryRepositoryPort abLogQueryRepositoryPort;

    @Override
    public List<AuxiliaryBookLog> findAllByAuxiliaryBookPublicId(String auxiliaryBookId) {
        return this.abLogQueryRepositoryPort.findAllByAuxiliaryBookPublicId(auxiliaryBookId);
    }
}
