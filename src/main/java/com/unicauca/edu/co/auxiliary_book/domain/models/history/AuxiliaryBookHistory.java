package com.unicauca.edu.co.auxiliary_book.domain.models.history;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EDeliveryWay;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @brief Modelo de dominio para el historial de un libro auxiliar.
 *
 * Registra un estado puntual del ciclo de vida del libro ({@link EState})
 * junto con el canal de entrega ({@link EDeliveryWay}) asociado, útil
 * para auditoría y trazabilidad.
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuxiliaryBookHistory {
    private Long id;
    private String publicId;
    private AuxiliaryBook auxiliaryBook;
    private EState state;
    private EDeliveryWay deliveryWay;
}
