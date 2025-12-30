package com.unicauca.edu.co.auxiliary_book.domain.ports;

/**
 * @brief Output port for formatting and returning error responses
 *
 * Defines the contract for formatting and delivering various types of error responses
 * to the client or calling layer.
 */
public interface IFormatterResultOutputPort {
    /**
     * @brief Returns a business rule error response
     * @param status HTTP status code to return
     * @param message Error message to include in the response
     */
    public void returnBusinessRuleErrorResponse(int status, String message);

    /**
     * @brief Returns an entity already exists error response
     * @param status HTTP status code to return
     * @param message Error message to include in the response
     */
    public void returnEntityAlreadyExistsErrorResponse(int status, String message);

    /**
     * @brief Returns an entity does not exist error response
     * @param status HTTP status code to return
     * @param message Error message to include in the response
     */
    public void returnEntityDoesNotExistErrorResponse(int status, String message);

    /**
     * @brief Returns a generic error response
     * @param status HTTP status code to return
     * @param message Error message to include in the response
     */
    public void returnErrorGenericResponse(int status, String message);
}
