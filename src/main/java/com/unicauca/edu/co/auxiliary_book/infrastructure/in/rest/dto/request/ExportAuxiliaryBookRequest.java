package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.request;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.export.AuxiliaryBookTemplate;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.AuxiliaryBookCriteria;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookFormat;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportAuxiliaryBookRequest {
    @NotNull(message = "The format of the report cannot be Empty")
    private EAuxiliaryBookFormat format; // e.g., "PDF", "Excel"

    @NotEmpty(message = "Enterprise name cannot be Empty")
    private String entName;

    @NotNull(message = "The criteria used to create the auxiliary book cannot be null")
    private AuxiliaryBookCriteria criteriaUsed;

    @NotNull(message = "Auxiliary Book Type cannot be null")
    private EAuxiliaryBookType auxBookType;

    @NotNull(message = "The data from the auxiliary book cannot be null")
    private List<?> auxBookData;

    @NotNull(message = "The template information for the report cannot be null")
    private AuxiliaryBookTemplate infoReportTemplate;
}
