package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class Voucher {
    private Long entId;
    private Integer sucId;
    private String code;
    private String name;
    private Integer number;
}
