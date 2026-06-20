package com.unicauca.edu.co.auxiliary_book.copy.domain.enums;

/**
 * Estados posibles de un trabajo de copia del módulo auxiliary-book.
 * Replica el contrato uniforme del orquestador (ADR-38, ADR-40).
 */
public enum CopyEstado {

    INICIADO,
    EN_PROCESO,
    COMPLETADO,
    COMPLETADO_CON_ADVERTENCIAS,
    FALLIDO,
    ERROR_NO_REINTENTABLE,
    CANCELADO
}
