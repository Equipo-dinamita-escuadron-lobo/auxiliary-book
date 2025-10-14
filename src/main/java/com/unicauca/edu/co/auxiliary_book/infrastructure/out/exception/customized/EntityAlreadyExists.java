package com.unicauca.edu.co.auxiliary_book.infrastructure.out.exception.customized;

/**
 * @brief Exception thrown when an entity already exists.
 *
 * Used to indicate that an attempt to create a duplicate entity was made.
 */
public class EntityAlreadyExists extends BaseException{

    /**
     * @brief Constructs a new EntityAlreadyExists exception with the specified error code and message.
     * @param errorCode HTTP status code or custom error code.
     * @param message Error message to describe the exception.
     */
    public EntityAlreadyExists(Integer errorCode, String message) {
        super(errorCode, message);
    }

}
