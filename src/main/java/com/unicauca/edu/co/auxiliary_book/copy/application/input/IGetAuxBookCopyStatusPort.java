package com.unicauca.edu.co.auxiliary_book.copy.application.input;

import com.unicauca.edu.co.auxiliary_book.copy.infrastructure.in.rest.dto.CopyStatusResponseDto;

/**
 * Puerto de entrada: consultar el estado de un proceso de copia de auxiliary-book.
 */
public interface IGetAuxBookCopyStatusPort {

    CopyStatusResponseDto obtenerEstado(String idProceso);
}
