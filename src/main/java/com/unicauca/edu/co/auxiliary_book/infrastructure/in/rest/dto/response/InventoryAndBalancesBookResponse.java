package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.response;

import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.models.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class InventoryAndBalancesBookResponse {
    private Account account;
    private String description;
    private Double value;
}
