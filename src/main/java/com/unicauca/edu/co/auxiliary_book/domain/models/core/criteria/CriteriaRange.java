package com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria;

import lombok.Getter;

@Getter
public class CriteriaRange <T extends Comparable<T>> {
    private T from;
    private T to;

    public CriteriaRange(T from, T to) {
        this.from = from;
        this.to = to;
    }

    public boolean isSingleValue() {
        return from != null && from.equals(to);
    }

}
