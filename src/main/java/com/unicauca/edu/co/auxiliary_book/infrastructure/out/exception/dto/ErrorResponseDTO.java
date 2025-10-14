package com.unicauca.edu.co.auxiliary_book.infrastructure.out.exception.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

/**
 * @brief Data Transfer Object for error responses.
 *
 * Represents the structure of error information returned to the client,
 * including HTTP status, message, request URL, and HTTP method.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDTO {
    /**
     * @brief HTTP status code of the error response.
     */
    private Integer status;

    /**
     * @brief Error message describing the problem.
     */
    private String message;

    /**
     * @brief URL of the request that caused the error.
     */
    private String url;

    /**
     * @brief HTTP method of the request that caused the error.
     */
    private String method;

    /**
     * @brief Builds a ResponseEntity containing this error response.
     * @return ResponseEntity with the error response and appropriate HTTP status.
     */
    public ResponseEntity<ErrorResponseDTO> of() {
        return ResponseEntity.status(this.status).body(this);
    }
}
