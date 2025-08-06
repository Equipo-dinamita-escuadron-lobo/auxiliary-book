package com.unicauca.edu.co.auxiliary_book.domain.models.log;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.management.ConstructorParameters;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuxiliaryBookLog {
    private Long id;
    private AuxiliaryBook book;
    private String logTypeEvent;
}
