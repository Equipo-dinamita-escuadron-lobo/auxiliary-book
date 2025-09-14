package com.unicauca.edu.co.auxiliary_book.infrastructure.out.exception.customized;

public class EntityAlreadyExists extends BaseException{

    public EntityAlreadyExists(Integer errorCode, String message) {
        super(errorCode, message);
    }

}
