package com.unicauca.edu.co.auxiliary_book.copy.application.services;

import com.unicauca.edu.co.auxiliary_book.copy.application.input.IGetAuxBookCopyStatusPort;
import com.unicauca.edu.co.auxiliary_book.copy.application.output.IAuxBookCopyJobLogRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.copy.domain.exceptions.DuplicateCopyJobException;
import com.unicauca.edu.co.auxiliary_book.copy.domain.models.CopyJobLog;
import com.unicauca.edu.co.auxiliary_book.copy.infrastructure.in.rest.dto.CopyStatusResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Servicio para consultar el estado de un proceso de copia de auxiliary-book.
 * ADR-38.
 */
@Service
@RequiredArgsConstructor
public class GetAuxBookCopyStatusService implements IGetAuxBookCopyStatusPort {

    private final IAuxBookCopyJobLogRepositoryPort logRepo;

    @Override
    public CopyStatusResponseDto obtenerEstado(String idProceso) {
        CopyJobLog log = logRepo.buscarPorIdProceso(idProceso)
                .orElseThrow(() -> new DuplicateCopyJobException(idProceso, 0));

        return CopyStatusResponseDto.builder()
                .fase(log.getFase() != null ? log.getFase() : 0)
                .estado(log.getEstado() != null ? log.getEstado().name() : "DESCONOCIDO")
                .registrosProcesados(log.getEquivalenciasGeneradas() != null ? log.getEquivalenciasGeneradas() : 0)
                .intentos(1)
                .ultimoError(log.getErrorMessage())
                .build();
    }
}
