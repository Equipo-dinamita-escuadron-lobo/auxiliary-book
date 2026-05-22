package com.unicauca.edu.co.auxiliary_book.copy.infrastructure.in.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO de response para la ejecución de una fase de copia de auxiliary-book.
 * Contrato uniforme ADR-38.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CopyPhaseResponseDto {

    private String estado;
    private int registrosProcesados;
    private List<CopyEquivalenciaDto> equivalenciasGeneradas;
    private String mensaje;
    private List<String> advertencias;

    /**
     * Payload exportado en modo BACKUP.
     * Contiene los datos serializados de libros auxiliares y reportes programados
     * para ser consumidos en la fase RESTORE.
     * Null en modos DUPLICATE y RESTORE.
     */
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    private Object datosExportados;
}
