package com.unicauca.edu.co.auxiliary_book.copy.application.input;

/**
 * Puerto de entrada: limpiar registros de log de un proceso de copia de auxiliary-book.
 */
public interface ICleanupAuxBookCopyPort {

    void limpiar(String idProceso);
}
