package com.unicauca.edu.co.auxiliary_book.application.dto.auxiliaryBook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private String nature;
    private Long accountCode;
    private String accountDescription;
}
