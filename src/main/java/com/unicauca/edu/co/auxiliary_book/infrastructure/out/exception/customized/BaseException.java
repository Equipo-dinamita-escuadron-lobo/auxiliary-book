package com.unicauca.edu.co.auxiliary_book.infrastructure.out.exception.customized;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseException extends RuntimeException {

    private Integer status;
    private String message;

    public BaseException(Integer status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }
}