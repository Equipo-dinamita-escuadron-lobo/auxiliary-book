package com.unicauca.edu.co.auxiliary_book.domain.models.core;

import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAlignment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuxiliaryBookTemplate {
    private Long id;
    private String name;
    private String PathLogotype;
    private EAlignment alineation;
    private String font;
    private String mainColor;
}
