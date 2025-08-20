package com.unicauca.edu.co.auxiliary_book.domain.models.core;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.AuxiliaryBookCriteria;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookFormat;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuxiliaryBook {
    private Long id;
    private EAuxiliaryBookType type;
    private String entId;
    private Long userId;
    private EAuxiliaryBookFormat format;
    private AuxiliaryBookTemplate template;
    private AuxiliaryBookCriteria criteria;
    private LocalDateTime createdAt;

    public void changeFormat(EAuxiliaryBookFormat format) {
        this.format = format;
    }
}
