package com.unicauca.edu.co.auxiliary_book.domain.ports;

/**
 * Puerto de salida para formatear y devolver respuestas de error.
 */
public interface IFormatterResultOutputPort {
    public void returnBusinessRuleErrorResponse(int status, String message);
    public void returnEntityAlreadyExistsErrorResponse(int status, String message);
    public void returnEntityDoesNotExistErrorResponse(int status, String message);
    public void returnErrorGenericResponse(int status, String message);
}
