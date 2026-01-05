package com.unicauca.edu.co.auxiliary_book.domain.models.core.export;

import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAlignment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

/**
 * @brief Domain model representing the export template for an auxiliary book.
 *
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuxiliaryBookTemplate {
    private Long id;
    private String name;
    private URL pathLogotype;
    private EAlignment alienation;
    private String font;
    private Integer fontSize;
    private String mainColor;
}
