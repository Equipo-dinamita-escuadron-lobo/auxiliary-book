package com.unicauca.edu.co.auxiliary_book.infrastructure.out.exception.customized;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @brief Enumeration of error codes used in custom exceptions.
 *
 * Provides standardized error codes and descriptions for exception handling.
 */
@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    /**
     * @brief Generic error code.
     */
    GENERIC_ERROR("GC-001: Generic error -> "),
    /**
     * @brief Error code for entity already exists.
     */
    ENTITY_ALREADY_EXISTS("GC-002: Entity already exists -> "),
    /**
     * @brief Error code for entity not found.
     */
    ENTITY_NOT_FOUND("GC-003: Entity not found -> "),
    /**
     * @brief Error code for business rule violation.
     */
    BUSINESS_RULE_VIOLATION("GC-004: Business rule violation -> ");

    /**
     * @brief Description of the error code.
     */
    private final String description;
}
