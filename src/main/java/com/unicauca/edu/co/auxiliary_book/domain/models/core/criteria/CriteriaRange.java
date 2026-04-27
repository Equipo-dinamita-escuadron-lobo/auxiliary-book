package com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria;

import lombok.Data;

/**
 * @brief Modelo de dominio que representa el rango del nivel de detalle.
 *
 * Define los límites inferior y superior (fromRange/toRange) utilizados
 * para filtrar cuentas según el nivel seleccionado (clase, grupo, cuenta,
 * subcuenta o cuenta auxiliar) al momento de generar un libro auxiliar.
 */
@Data
public class CriteriaRange{
    private Long fromRange;
    private Long toRange;

    public CriteriaRange(Long from, Long to) {
        this.fromRange = from;
        this.toRange = to;
    }

    public boolean isSingleValue() {
        return fromRange != null && fromRange.equals(toRange);
    }

}
