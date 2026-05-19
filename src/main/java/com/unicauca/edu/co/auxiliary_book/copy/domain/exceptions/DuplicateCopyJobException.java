package com.unicauca.edu.co.auxiliary_book.copy.domain.exceptions;

/**
 * Excepción lanzada cuando no se encuentra un proceso de copia de auxiliary-book.
 * ADR-38.
 */
public class DuplicateCopyJobException extends RuntimeException {

    public DuplicateCopyJobException(String idProceso, int fase) {
        super(String.format("No se encontró proceso de copia auxbook con idProceso=%s fase=%d",
                idProceso, fase));
    }
}
