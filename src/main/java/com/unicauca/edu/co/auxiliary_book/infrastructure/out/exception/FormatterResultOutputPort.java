package com.unicauca.edu.co.auxiliary_book.infrastructure.out.exception;

import com.unicauca.edu.co.auxiliary_book.domain.ports.IFormatterResultOutputPort;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.exception.handler.BusinessRuleException;
import org.springframework.stereotype.Service;

@Service
public class FormatterResultOutputPort implements IFormatterResultOutputPort {

    @Override
    public void returnResponseError(int status, String message) {
        System.out.println(status + " - " + message);
        throw new BusinessRuleException(status, message);
    }

}
