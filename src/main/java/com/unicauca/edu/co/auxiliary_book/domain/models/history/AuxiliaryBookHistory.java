package com.unicauca.edu.co.auxiliary_book.domain.models.history;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EDeliveryWay;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuxiliaryBookHistory {
    private Long id;
    private AuxiliaryBook book;
    private EState state;

    private EDeliveryWay deliveryWay;
}
