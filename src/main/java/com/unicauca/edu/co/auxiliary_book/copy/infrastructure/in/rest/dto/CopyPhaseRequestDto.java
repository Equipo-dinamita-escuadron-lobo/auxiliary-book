package com.unicauca.edu.co.auxiliary_book.copy.infrastructure.in.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * DTO de request para ejecutar una fase de copia de auxiliary-book.
 * Contrato uniforme ADR-38.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CopyPhaseRequestDto {

    @NotNull
    private UUID idProceso;

    @Positive
    private int fase;

    @NotBlank
    private String entOrigen;

    private String entDestino;

    @NotNull
    private Instant snapshotCorte;

    /**
     * Equivalencias generadas por participantes anteriores.
     * AuxBook usa:
     * - tabla "cuentaContable" (CATALOGUE) para remapear accountId en criteria
     * - tabla "third" (THIRDS) para remapear thirdPartyId en criteria
     * ADR-40.
     */
    private List<CopyEquivalenciaDto> equivalenciasPrev;

    /**
     * Payload JSON exportado por la fase BACKUP.
     * Presente en modo RESTORE; null en modos BACKUP y DUPLICATE.
     */
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    private Object datosImportados;
}
