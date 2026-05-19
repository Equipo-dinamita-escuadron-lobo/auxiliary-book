package com.unicauca.edu.co.auxiliary_book.copy.infrastructure.in.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de response para cancelar un proceso de copia de auxiliary-book.
 * ADR-38.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CopyCancelResponseDto {

    private String estado;
    private String mensaje;
}
