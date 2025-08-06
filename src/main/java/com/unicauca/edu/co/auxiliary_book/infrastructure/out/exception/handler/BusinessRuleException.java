package com.unicauca.edu.co.auxiliary_book.infrastructure.out.exception.handler;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessRuleException extends BaseException {

    public BusinessRuleException(int status, String message) {
        super(status, message);
    }
}
