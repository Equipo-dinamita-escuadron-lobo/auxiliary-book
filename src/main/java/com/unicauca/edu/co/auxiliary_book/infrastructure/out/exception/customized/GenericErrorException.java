package com.unicauca.edu.co.auxiliary_book.infrastructure.out.exception.customized;

/**
 * @brief Exception thrown for generic errors.
 *
 * Used to indicate an unspecified or general error condition.
 */
public class GenericErrorException extends BaseException {

    /**
     * @brief Constructs a new GenericErrorException with the specified status and message.
     * @param status HTTP status code to associate with the exception.
     * @param message Error message to describe the exception.
     */
    public GenericErrorException(Integer status, String message) {
        super(status, message);
    }

}