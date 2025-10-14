package com.unicauca.edu.co.auxiliary_book.infrastructure.out.exception;

import com.unicauca.edu.co.auxiliary_book.domain.ports.IFormatterResultOutputPort;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.exception.customized.*;
import org.springframework.stereotype.Service;

/**
 * @brief Output port implementation for formatting and throwing error responses.
 *
 * Provides methods to throw custom exceptions for various error scenarios,
 * such as business rule violations, entity existence conflicts, and generic errors.
 */
@Service
public class FormatterResultOutputPort implements IFormatterResultOutputPort {

    /**
     * @brief Throws a BusinessRuleException for business rule violations.
     * @param status HTTP status code to return.
     * @param message Error message to include in the exception.
     */
    @Override
    public void returnBusinessRuleErrorResponse(int status, String message) {
        throw new BusinessRuleException(status, ErrorCode.BUSINESS_RULE_VIOLATION.getDescription()  + message);
    }

    /**
     * @brief Throws an EntityAlreadyExists exception when an entity already exists.
     * @param status HTTP status code to return.
     * @param message Error message to include in the exception.
     */
    @Override
    public void returnEntityAlreadyExistsErrorResponse(int status, String message) {
        throw new EntityAlreadyExists(status, ErrorCode.ENTITY_ALREADY_EXISTS.getDescription() + message);
    }

    /**
     * @brief Throws an EntityDoesNotExistException when an entity is not found.
     * @param status HTTP status code to return.
     * @param message Error message to include in the exception.
     */
    @Override
    public void returnEntityDoesNotExistErrorResponse(int status, String message) {
        throw new EntityDoesNotExistException(status, ErrorCode.ENTITY_NOT_FOUND.getDescription() + message);
    }

    /**
     * @brief Throws a GenericErrorException for generic error scenarios.
     * @param status HTTP status code to return.
     * @param message Error message to include in the exception.
     */
    @Override
    public void returnErrorGenericResponse(int status, String message) {
        throw new GenericErrorException(status, ErrorCode.GENERIC_ERROR.getDescription() + message);
    }

}
