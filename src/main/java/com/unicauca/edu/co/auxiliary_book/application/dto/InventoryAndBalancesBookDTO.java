package com.unicauca.edu.co.auxiliary_book.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @brief Data Transfer Object for Inventory and Balances Book entries
 *
 * Represents the structure for transferring inventory and balances book output data
 * between application layers.
 */
@Data
@AllArgsConstructor @NoArgsConstructor
public class InventoryAndBalancesBookDTO {
    /**
     * @brief Account related with the book
     */
    private AccountDTO account;

    /**
     * @brief Additional description for the output
     */
    private String description;

    /**
     * @brief Value associated with the output
     */
    private BigDecimal value;

    public Long getAccountCode() {
        return account != null ? account.getAccountCode() : null;
    }

    public String getAccountDescription() {
        return account != null ? account.getAccountDescription() : null;
    }
}
