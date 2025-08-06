package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO<T> {
    private T data;
    private Integer statusCode;
    private String message;

    public ResponseEntity<ResponseDTO<T>> of() {
        return ResponseEntity.status(this.statusCode).body(this);
    }
}
