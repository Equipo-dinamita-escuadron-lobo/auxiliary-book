package com.unicauca.edu.co.auxiliary_book.copy.application.output;

import com.unicauca.edu.co.auxiliary_book.copy.domain.models.CopyJobLog;

import java.util.Optional;

/**
 * Puerto de salida: persistencia del log de idempotencia de copia de auxiliary-book.
 * ADR-38.
 */
public interface IAuxBookCopyJobLogRepositoryPort {

    CopyJobLog guardar(CopyJobLog log);

    Optional<CopyJobLog> buscarPorIdProcesoYFase(String idProceso, int fase);

    Optional<CopyJobLog> buscarPorIdProceso(String idProceso);

    void eliminarPorIdProceso(String idProceso);
}
