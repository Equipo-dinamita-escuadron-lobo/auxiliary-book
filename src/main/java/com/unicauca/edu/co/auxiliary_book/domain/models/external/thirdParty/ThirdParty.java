package com.unicauca.edu.co.auxiliary_book.domain.models.external.thirdParty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class ThirdParty {
    private Long id;
    private String name;
    private Long identificationNumber;
    private String typeId;
}
