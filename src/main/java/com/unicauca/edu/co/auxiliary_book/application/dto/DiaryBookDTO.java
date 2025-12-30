package com.unicauca.edu.co.auxiliary_book.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @brief Data Transfer Object for Diary Book entries
 *
 * Represents the structure for transferring diary book output data
 * between application layers.
 */
@Data
@AllArgsConstructor @NoArgsConstructor
public class DiaryBookDTO {
    /**
     * @brief Date of the diary book output
     */
    private Date date;

    /**
     * @brief Account realted with accounting info
     */
    private AccountDTO account;

    /**
     * @brief Name of the voucher associated with the output
     */
    private String voucherName;

    /**
     * @brief Number of the voucher associated with the output
     */
    private String voucherNumber;

    /**
     * @brief Debit amount for the output
     */
    private BigDecimal debit;

    /**
     * @brief Credit amount for the output
     */
    private BigDecimal credit;

    public Long getAccountCode() {
        return account != null ? account.getAccountCode() : null;
    }

    public String getAccountDescription() {
        return account != null ? account.getAccountDescription() : null;
    }
}
