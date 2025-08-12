package com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria;

import lombok.Data;

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
