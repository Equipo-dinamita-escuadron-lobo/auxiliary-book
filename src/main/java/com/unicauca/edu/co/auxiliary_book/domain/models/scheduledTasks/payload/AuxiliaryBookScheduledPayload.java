package com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.payload;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.export.AuxiliaryBookTemplate;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookFormat;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EDeliveryWay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload used to schedule auxiliary book generation tasks.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuxiliaryBookScheduledPayload {

    private EDeliveryWay deliveryWay;
    private String emailTo;
    private String emailSubject;
    private String emailBody;
    private EAuxiliaryBookFormat format;
    private String entName;
    private AuxiliaryBook auxiliaryBook;
    private AuxiliaryBookTemplate template;
}
