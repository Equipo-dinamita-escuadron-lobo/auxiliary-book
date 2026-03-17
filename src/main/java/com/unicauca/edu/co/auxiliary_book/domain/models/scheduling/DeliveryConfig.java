package com.unicauca.edu.co.auxiliary_book.domain.models.scheduling;

import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookFormat;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EDeliveryWay;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryConfig {
    private EDeliveryWay deliveryWay;
    private EAuxiliaryBookFormat format;
    private EmailConfig emailConfig;
}
