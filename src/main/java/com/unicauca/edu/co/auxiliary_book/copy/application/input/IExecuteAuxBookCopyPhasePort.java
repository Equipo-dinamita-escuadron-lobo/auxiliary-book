package com.unicauca.edu.co.auxiliary_book.copy.application.input;

import com.unicauca.edu.co.auxiliary_book.copy.infrastructure.in.rest.dto.CopyPhaseRequestDto;
import com.unicauca.edu.co.auxiliary_book.copy.infrastructure.in.rest.dto.CopyPhaseResponseDto;

/**
 * Puerto de entrada: ejecutar una fase de copia del módulo auxiliary-book.
 * ADR-38, ADR-40.
 */
public interface IExecuteAuxBookCopyPhasePort {

    CopyPhaseResponseDto ejecutar(CopyPhaseRequestDto request);
}
