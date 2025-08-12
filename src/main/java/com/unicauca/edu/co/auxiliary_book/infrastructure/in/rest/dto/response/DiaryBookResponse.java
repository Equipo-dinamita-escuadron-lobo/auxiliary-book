package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.response;

import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.models.AccountDTO;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.models.VoucherDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor @NoArgsConstructor
public class DiaryBookResponse {
    private LocalDate date;
    private AccountDTO account;
    private VoucherDTO voucher;
    private Double debit;
    private Double credit;
}
