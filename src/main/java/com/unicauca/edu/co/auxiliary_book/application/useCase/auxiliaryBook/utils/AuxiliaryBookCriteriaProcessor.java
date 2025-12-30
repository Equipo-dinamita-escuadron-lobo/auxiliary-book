package com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.unicauca.edu.co.auxiliary_book.application.ports.out.IAccountingInfoClient;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.AuxiliaryBookCriteria;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.CriteriaRange;
import com.unicauca.edu.co.auxiliary_book.domain.models.external.accountingInfo.AccountingInfo;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class AuxiliaryBookCriteriaProcessor {

    public List<AccountingInfo> processAccountingInfo(IAccountingInfoClient queryPort, AuxiliaryBook book) {
        AuxiliaryBookCriteria criteria = book.getCriteria();
        List<AccountingInfo> allAccountData = queryPort.getAllAccountInfo();
        return this.filterAccountingInfoByCriteria(book,criteria,allAccountData);
    }

    private boolean isInRange(Long value, CriteriaRange range) {
        return (range.getFromRange() == null || value.compareTo(range.getFromRange()) >= 0)
                && (range.getToRange() == null || value.compareTo(range.getToRange()) <= 0);
    }

    private boolean isWithinDateRange(AccountingInfo info, AuxiliaryBookCriteria criteria) {
        // --- INICIO DE LA CORRECCIÓN ---

        // 1. Convertir java.util.Date (con posible hora/min/seg) a java.time.LocalDate
        java.util.Date movementDateUtil = info.getDate();
        LocalDate movementDate = movementDateUtil.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        // 2. Obtener las fechas del criterio (que ya son LocalDate)
        LocalDate startDate = criteria.getStartDate();
        LocalDate endDate = criteria.getEndDate();

        // 3. Comparar LocalDate vs LocalDate (esta es una comparación segura)
        // !isBefore significa "en o después de" (>=)
        boolean afterStart = startDate == null || !movementDate.isBefore(startDate);
        // !isAfter significa "en o antes de" (<=)
        boolean beforeEnd = endDate == null || !movementDate.isAfter(endDate);

        return afterStart && beforeEnd;
        // --- FIN DE LA CORRECCIÓN ---
    }

    private boolean matchesCriteria(AccountingInfo info, AuxiliaryBookCriteria criteria) {
        String accCode = info.getAccount().getCode().toString();
        var range = criteria.getCriteriaRange();

        switch (criteria.getCriteriaType()) {
            case NUMBER_CLASS -> {
                long classDigit = Integer.parseInt(accCode.substring(0, 1));
                return isInRange(classDigit, range);
            }
            case GROUP -> {
                long groupDigits = Integer.parseInt(accCode.substring(0, 2));
                return isInRange(groupDigits, range);
            }
            case ACCOUNT -> {
                long accDigits = Integer.parseInt(accCode.substring(0, 4));
                return isInRange(accDigits, range);
            }
            case SUB_ACCOUNT -> {
                long subAcc = Integer.parseInt(accCode.substring(0, 6));
                return isInRange(subAcc, range);
            }
            case AUXILIARY_ACCOUNT -> {
                long aux = Integer.parseInt(accCode.length() >= 8 ? accCode.substring(0, 8) : accCode);
                return isInRange(aux, range);
            }
            default -> throw new IllegalArgumentException("Criterio no reconocido");
        }
    }

    private List<AccountingInfo> filterAccountingInfoByCriteria(
            AuxiliaryBook book,
            AuxiliaryBookCriteria criteria,
            List<AccountingInfo> all
    ) {
        return all.parallelStream()
                .filter(info -> info != null && info.getEntId() != null && info.getDate() != null)
                .filter(info -> Objects.equals(info.getEntId(), book.getEntId()))
                .filter(info -> isWithinDateRange(info, criteria))
                .filter(info -> !criteria.hasRange() || matchesCriteria(info, criteria))
                .filter(info -> {
                    if (criteria.getThirdPartyId() == null) return true;
                    if (info.getThirdPartyId() == null) return false;
                    return info.getThirdPartyId().trim().equals(criteria.getThirdPartyId().trim());
                })
                .filter(info -> {
                    if (criteria.getCostCenterId() == null) return true; // No hay filtro, pasa
                    if (info.getCostCenter() == null || info.getCostCenter().getCode() == null) return false; // El dato no tiene C.Costo, no pasa
                    return info.getCostCenter().getCode().equals(criteria.getCostCenterId());
                })
                .filter(info -> {
                    if (criteria.getVoucherType() == null) return true; // No hay filtro, pasa
                    if (info.getVoucher() == null || info.getVoucher().getType() == null) return false; // El dato no tiene Voucher/Tipo, no pasa
                    return info.getVoucher().getType().equals(criteria.getVoucherType());
                })

                .toList();
    }
}