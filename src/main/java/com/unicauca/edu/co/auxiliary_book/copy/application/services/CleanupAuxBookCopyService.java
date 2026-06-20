package com.unicauca.edu.co.auxiliary_book.copy.application.services;

import com.unicauca.edu.co.auxiliary_book.copy.application.input.ICleanupAuxBookCopyPort;
import com.unicauca.edu.co.auxiliary_book.copy.application.output.IAuxBookCopyJobLogRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Servicio para limpiar los registros de log de un proceso de copia de auxiliary-book.
 * ADR-38.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CleanupAuxBookCopyService implements ICleanupAuxBookCopyPort {

    private final IAuxBookCopyJobLogRepositoryPort logRepo;

    @Override
    public void limpiar(String idProceso) {
        log.info("Limpiando registros de copia auxiliary-book para proceso {}", idProceso);
        logRepo.eliminarPorIdProceso(idProceso);
    }
}
