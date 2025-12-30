package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class VoucherDTO {
    private String number;
    private String type;
}
