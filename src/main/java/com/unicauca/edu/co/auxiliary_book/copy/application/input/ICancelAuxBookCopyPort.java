package com.unicauca.edu.co.auxiliary_book.copy.application.input;

import com.unicauca.edu.co.auxiliary_book.copy.infrastructure.in.rest.dto.CopyCancelResponseDto;

/**
 * Puerto de entrada: cancelar un proceso de copia de auxiliary-book.
 */
public interface ICancelAuxBookCopyPort {

    CopyCancelResponseDto cancelar(String idProceso);
}
