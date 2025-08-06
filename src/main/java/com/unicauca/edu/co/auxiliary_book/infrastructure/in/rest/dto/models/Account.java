package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class Account {
    private Integer code;
    private String description;
}
