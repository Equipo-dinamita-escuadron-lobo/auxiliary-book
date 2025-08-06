package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.response;

import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.models.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class MayorAndBalancesBookResponse {
    private Account account;
    //Previous balance
    private Double previousBalanceDebit;
    private Double previousBalanceCredit;
    //Movement
    private Double movementDebit;
    private Double movementCredit;
    //New balance
    private Double newBalanceDebit;
    private Double newBalanceCredit;
}
