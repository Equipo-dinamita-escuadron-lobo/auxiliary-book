package com.unicauca.edu.co.auxiliary_book.domain.models.enums;

/**
 * @brief Enumeration of criteria types for auxiliary books.
 *
 */
public enum ECriteriaType {
    NUMBER_CLASS("Clase"),
    GROUP("Grupo"),
    ACCOUNT("Cuenta"),
    SUB_ACCOUNT("Subcuenta"),
    AUXILIARY_ACCOUNT("Cuenta Auxiliar");

    private final String displayName;

    ECriteriaType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
