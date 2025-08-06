package com.unicauca.edu.co.auxiliary_book.infrastructure.out.exception.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDTO<T> {
    private T data;
    private Integer errorCode;
    private String message;

    public ResponseEntity<ErrorResponseDTO<T>> of(){
        return ResponseEntity.status(this.errorCode).body(this);
    }
}
