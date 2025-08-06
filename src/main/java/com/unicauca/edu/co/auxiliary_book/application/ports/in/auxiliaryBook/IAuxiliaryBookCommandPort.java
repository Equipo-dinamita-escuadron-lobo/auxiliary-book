package com.unicauca.edu.co.auxiliary_book.application.ports.in.auxiliaryBook;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;

import java.util.List;

public interface IAuxiliaryBookCommandPort {
    AuxiliaryBook registerAuxiliaryBook(AuxiliaryBook auxiliaryBook);
    List<?> genereteAuxiliaryBookInfo(AuxiliaryBook auxiliaryBook);
}
