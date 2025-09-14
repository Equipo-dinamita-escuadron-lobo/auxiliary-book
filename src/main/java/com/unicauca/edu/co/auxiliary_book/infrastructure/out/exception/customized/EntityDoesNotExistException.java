package com.unicauca.edu.co.auxiliary_book.infrastructure.out.exception.customized;

import lombok.Getter;

@Getter
public class EntityDoesNotExistException extends BaseException {

    public EntityDoesNotExistException(Integer status, String message) {
        super(status, message);
    }

}
