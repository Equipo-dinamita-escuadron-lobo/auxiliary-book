package com.unicauca.edu.co.auxiliary_book.infrastructure.out.exception;

import com.unicauca.edu.co.auxiliary_book.domain.ports.IFormatterResultOutputPort;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.exception.customized.*;
import org.springframework.stereotype.Service;

@Service
public class FormatterResultOutputPort implements IFormatterResultOutputPort {

    @Override
    public void returnBusinessRuleErrorResponse(int status, String message) {
        throw new BusinessRuleException(status, ErrorCode.BUSINESS_RULE_VIOLATION.getDescription()  + message);
    }

    @Override
    public void returnEntityAlreadyExistsErrorResponse(int status, String message) {
        throw new EntityAlreadyExists(status, ErrorCode.ENTITY_ALREADY_EXISTS.getDescription() + message);
    }

    @Override
    public void returnEntityDoesNotExistErrorResponse(int status, String message) {
        throw new EntityDoesNotExistException(status, ErrorCode.ENTITY_NOT_FOUND.getDescription() + message);
    }

    @Override
    public void returnErrorGenericResponse(int status, String message) {
        throw new GenericErrorException(status, ErrorCode.GENERIC_ERROR.getDescription() + message);
    }

}
