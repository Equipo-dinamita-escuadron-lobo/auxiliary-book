package com.unicauca.edu.co.auxiliary_book.infrastructure.out.exception.handler;

import java.util.HashMap;
import java.util.Map;

import com.unicauca.edu.co.auxiliary_book.infrastructure.out.exception.customized.*;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.exception.dto.ErrorResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;


/**
 * Global Exception Handler to manage various exception types.
 * @author JulianRuano
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles exceptions.
     * Logs the error message and returns a response for this specific exception.
     *
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName;
            if (error instanceof FieldError) {
                fieldName = ((FieldError) error).getField();
            } else {
                fieldName = error.getObjectName();
            }
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(errors);
    }


    /**
     * Handles custom exceptions that have a common structure.
     * Logs the error message and returns a consistent response.
     */
    @ExceptionHandler({
            BusinessRuleException.class,
            EntityAlreadyExists.class,
            EntityDoesNotExistException.class,
            GenericErrorException.class
    })
    public ResponseEntity<ErrorResponseDTO> handleCustomExceptions(final HttpServletRequest req, BaseException e) {
        return buildErrorResponse(e.getStatus(), e.getMessage(), req);
    }

    /**
     * Handles various bad request exceptions.
     */
    @ExceptionHandler({
            NoResourceFoundException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,
            IllegalArgumentException.class,
            EntityNotFoundException.class
    })
    public ResponseEntity<ErrorResponseDTO> handleBadRequestExceptions(final HttpServletRequest req,Exception ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = ex.getMessage();

        if (ex instanceof NoResourceFoundException) {
            status = HttpStatus.NOT_FOUND;
            message = "Resource not found";
        } else if (ex instanceof MissingServletRequestParameterException) {
            message = "Missing parameter: " + ((MissingServletRequestParameterException) ex).getParameterName();
        } else if (ex instanceof MethodArgumentTypeMismatchException) {
            message = "Method argument type mismatch for '" + ((MethodArgumentTypeMismatchException) ex).getName() + "'";
        }

        return buildErrorResponse(status.value(), message, req);
    }

    /**
     * Handles runtime exceptions.
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponseDTO> handleRuntimeException(final HttpServletRequest req, RuntimeException ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error", req);
    }

    /**
     * Helper method to build a consistent error response.
     */
    private ResponseEntity<ErrorResponseDTO> buildErrorResponse(int status, String message, HttpServletRequest req) {
        return ErrorResponseDTO.builder()
                .status(status)
                .message(message)
                .url(req.getRequestURI())
                .method(req.getMethod())
                .build()
                .of();
    }
}
