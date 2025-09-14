package com.unicauca.edu.co.auxiliary_book.infrastructure.out.exception.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDTO {
    private Integer status;
    private String message;
    private String url;
    private String method;

    public ResponseEntity<ErrorResponseDTO> of() {
        return ResponseEntity.status(this.status).body(this);
    }
}
