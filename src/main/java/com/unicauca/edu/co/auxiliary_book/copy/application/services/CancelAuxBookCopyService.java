package com.unicauca.edu.co.auxiliary_book.copy.application.services;

import com.unicauca.edu.co.auxiliary_book.copy.application.input.ICancelAuxBookCopyPort;
import com.unicauca.edu.co.auxiliary_book.copy.application.output.IAuxBookCopyJobLogRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.copy.domain.enums.CopyEstado;
import com.unicauca.edu.co.auxiliary_book.copy.domain.exceptions.DuplicateCopyJobException;
import com.unicauca.edu.co.auxiliary_book.copy.domain.models.CopyJobLog;
import com.unicauca.edu.co.auxiliary_book.copy.infrastructure.in.rest.dto.CopyCancelResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Servicio para cancelar un proceso de copia de auxiliary-book.
 * ADR-38.
 */
@Service
@RequiredArgsConstructor
public class CancelAuxBookCopyService implements ICancelAuxBookCopyPort {

    private final IAuxBookCopyJobLogRepositoryPort logRepo;

    @Override
    public CopyCancelResponseDto cancelar(String idProceso) {
        CopyJobLog log = logRepo.buscarPorIdProceso(idProceso)
                .orElseThrow(() -> new DuplicateCopyJobException(idProceso, 0));

        CopyJobLog cancelado = CopyJobLog.builder()
                .idProceso(log.getIdProceso())
                .fase(log.getFase())
                .modulo(log.getModulo())
                .estado(CopyEstado.CANCELADO)
                .fechaInicio(log.getFechaInicio())
                .fechaFin(Instant.now())
                .equivalenciasGeneradas(log.getEquivalenciasGeneradas())
                .build();
        logRepo.guardar(cancelado);

        return CopyCancelResponseDto.builder()
                .estado(CopyEstado.CANCELADO.name())
                .mensaje("Proceso de copia auxiliary-book cancelado exitosamente")
                .build();
    }
}
