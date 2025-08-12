package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.response;

import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.models.AccountDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class InventoryAndBalancesBookResponse {
    private AccountDTO account;
    private String description;
    private Double value;
}
