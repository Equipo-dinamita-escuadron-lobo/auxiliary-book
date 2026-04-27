package com.unicauca.edu.co.auxiliary_book.domain.models.enums;

/**
 * @brief Enumeración del estado de entrega de una ejecución de reporte
 * programado (sin entrega, listo para descarga, email enviado,
 * email fallido, fallido).
 */
public enum EDeliveryStatus {
    NONE,
    READY_FOR_DOWNLOAD,
    EMAIL_SENT,
    EMAIL_FAILED,
    FAILED,
}
