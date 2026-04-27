package com.unicauca.edu.co.auxiliary_book.domain.models.external.accountingInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @brief Modelo de dominio con la información contable proveniente del servicio externo.
 *
 * Cada instancia representa un movimiento contable completo: entidad,
 * fecha, comprobante, cuenta, tercero, movimiento (débito/crédito) y
 * centro de costo. Es la materia prima para procesar los libros auxiliares.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountingInfo {
    private String entId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date date;

    private Voucher voucher;
    private Account account;
    private String thirdPartyId;
    private AccountingMovement accountingMovement;
    private CostCenter costCenter;
}
