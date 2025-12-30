package com.unicauca.edu.co.auxiliary_book.infrastructure.out.security;

/**
 * @brief Utility interface for extracting information from JWT tokens.
 *
 * Defines the contract for obtaining user or subject identifiers from JWT tokens.
 */
public interface IJwtUtils {
    /**
     * @brief Retrieves the identifier (subject) from the JWT token.
     * @return The identifier of the authenticated user.
     */
    String getId();
}
