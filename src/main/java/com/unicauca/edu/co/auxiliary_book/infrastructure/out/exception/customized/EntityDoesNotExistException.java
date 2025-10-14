package com.unicauca.edu.co.auxiliary_book.infrastructure.out.exception.customized;

import lombok.Getter;

/**
 * @brief Exception thrown when an entity does not exist.
 *
 * Used to indicate that a requested entity could not be found.
 */
@Getter
public class EntityDoesNotExistException extends BaseException {

    /**
     * @brief Constructs a new EntityDoesNotExistException with the specified status and message.
     * @param status HTTP status code to associate with the exception.
     * @param message Error message to describe the exception.
     */
    public EntityDoesNotExistException(Integer status, String message) {
        super(status, message);
    }

}
