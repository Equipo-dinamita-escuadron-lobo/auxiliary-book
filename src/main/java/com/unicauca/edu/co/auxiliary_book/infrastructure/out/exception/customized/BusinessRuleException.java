package com.unicauca.edu.co.auxiliary_book.infrastructure.out.exception.customized;

import lombok.Getter;
import lombok.Setter;

/**
 * @brief Exception thrown when a business rule is violated.
 *
 * Used to indicate that a business rule constraint has been broken.
 */
@Getter
@Setter
public class BusinessRuleException extends BaseException {

    /**
     * @brief Constructs a new BusinessRuleException with the specified status and message.
     * @param status HTTP status code to associate with the exception.
     * @param message Error message to describe the business rule violation.
     */
    public BusinessRuleException(Integer status, String message) {
        super(status, message);
    }
}
