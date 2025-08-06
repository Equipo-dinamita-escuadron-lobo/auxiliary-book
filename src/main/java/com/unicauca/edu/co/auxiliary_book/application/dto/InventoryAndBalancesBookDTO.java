package com.unicauca.edu.co.auxiliary_book.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class InventoryAndBalancesBookDTO {
    //Account
    private String accountCode;
    private String accountDescription;
    private String description;
    private Double value;
}
