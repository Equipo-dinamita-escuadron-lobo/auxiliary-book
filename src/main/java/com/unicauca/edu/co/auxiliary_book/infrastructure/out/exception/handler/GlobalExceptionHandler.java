package com.unicauca.edu.co.auxiliary_book.infrastructure.out.exception.handler;

import java.util.HashMap;
import java.util.Map;

import com.unicauca.edu.co.auxiliary_book.infrastructure.out.exception.dto.ErrorResponseDTO;
import jakarta.persistence.EntityNotFoundException;
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
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles exceptions.
     * Logs the error message and returns a response for this specific exception.
     *
     * @param ex The Exception instance.
     * @return Response entity containing error details.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.printStackTrace();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Handles exceptions.
     * Logs the error message and returns a response for this specific exception.
     *
     * @param ex The HttpRequestMethodNotSupportedException instance.
     * @return Response entity containing error details.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponseDTO<Object>> handleNoResourceFoundException(NoResourceFoundException ex) {
        ex.printStackTrace();
        return ErrorResponseDTO.builder()
                .errorCode(HttpStatus.NOT_FOUND.value())
                .message("Resource not found")
                .build()
                .of();
    }

    /**
     * Handles exceptions.
     * Logs the error message and returns a response for this specific exception.
     *
     * @param ex The BusinessRuleException instance.
     * @return Response entity containing error details.
     */
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponseDTO<Object>> handleBusinessRuleException(BusinessRuleException ex) {
        ex.printStackTrace();
        return ErrorResponseDTO.builder()
                .errorCode(ex.getStatus())
                .message(ex.getMessage())
                .build()
                .of();
    }


    /**
     * Handles MissingServletRequestParameterException.
     * Logs the error and returns a response entity with error details.
     *
     * @param ex The MissingServletRequestParameterException instance.
     * @return Response entity containing error details.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDTO<Object>> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        ex.printStackTrace();
        return ErrorResponseDTO.builder()
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .message("Missing parameter")
                .build()
                .of();
    }


    /**
     * Handles RuntimeException.
     * Logs the error and returns a response entity with error details.
     *
     * @param ex The RuntimeException instance.
     * @return Response entity containing error details.
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponseDTO<Object>> handleRuntimeException(RuntimeException ex) {
        ex.printStackTrace();
        return ErrorResponseDTO.builder()
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Internal server error")
                .build()
                .of();

    }


    /**
     * Handles `MethodArgumentTypeMismatchException` by returning a response with a 400 BAD REQUEST status.
     *
     * @param ex the exception thrown when a method argument type mismatch occurs.
     * @return a response with the error details.
     */

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDTO<Object>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        ex.printStackTrace();
        return ErrorResponseDTO.builder()
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .message("Method argument type mismatch")
                .build()
                .of();
    }

    /**
     * Handles `EntityNotFoundException` by returning a response with a 404 NOT FOUND status.
     *
     * @param ex the exception thrown when an entity is not found.
     * @return a response with the error details.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponseDTO<Object>> handleEntityNotFoundException(EntityNotFoundException ex) {
        ex.printStackTrace();
        return ErrorResponseDTO.builder()
                .errorCode(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build()
                .of();
    }


}
