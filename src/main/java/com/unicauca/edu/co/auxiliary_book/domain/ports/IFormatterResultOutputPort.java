package com.unicauca.edu.co.auxiliary_book.domain.ports;

/**
 * Puerto de salida para formatear y devolver respuestas de error.
 */
public interface IFormatterResultOutputPort {
    /**
     * Devuelve una respuesta de error con un código de estado y un mensaje.
     *
     * @param status  el código de estado HTTP de la respuesta
     * @param message el mensaje descriptivo del error
     */
    void returnResponseError(int status, String message);
}
