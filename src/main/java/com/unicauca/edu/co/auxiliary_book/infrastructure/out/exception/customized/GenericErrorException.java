package com.unicauca.edu.co.auxiliary_book.infrastructure.out.exception.customized;

public class GenericErrorException extends BaseException {

    public GenericErrorException(Integer status, String message) {
        super(status, message);
    }

}