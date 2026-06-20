package com.unicauca.edu.co.auxiliary_book.copy.infrastructure.in.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de equivalencia: tabla + idViejo → idNuevo.
 * Contrato uniforme ADR-38.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CopyEquivalenciaDto {

    private String modulo;
    private String tabla;
    private String idViejo;
    private String idNuevo;
}
