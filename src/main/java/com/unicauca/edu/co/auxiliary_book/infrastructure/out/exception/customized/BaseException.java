package com.unicauca.edu.co.auxiliary_book.infrastructure.out.exception.customized;

import lombok.Getter;
import lombok.Setter;

/**
 * @brief Base class for custom runtime exceptions in the application.
 *
 * Provides a structure for exceptions with an HTTP status and a message.
 */
@Getter
@Setter
public abstract class BaseException extends RuntimeException {

    /**
     * @brief HTTP status code associated with the exception.
     */
    private Integer status;

    /**
     * @brief Error message describing the exception.
     */
    private String message;

    /**
     * @brief Constructs a new BaseException with the specified status and message.
     * @param status HTTP status code to associate with the exception.
     * @param message Error message to describe the exception.
     */
    public BaseException(Integer status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }
}