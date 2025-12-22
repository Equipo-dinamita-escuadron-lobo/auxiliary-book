package com.unicauca.edu.co.auxiliary_book.domain.models.core.export;

import java.util.List;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @brief Domain model representing the information to export an auxiliary book.
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportInfo {
    private EAuxiliaryBookFormat format;
    private String entName;
    private AuxiliaryBook auxiliaryBook;
    private List<?> auxBookData;
    private AuxiliaryBookTemplate infoReportTemplate;
}
