package com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils;

import com.unicauca.edu.co.auxiliary_book.application.ports.out.IAccountingInfoClient;
import com.unicauca.edu.co.auxiliary_book.domain.models.external.accountingInfo.AccountingInfo;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.AuxiliaryBookCriteria;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.CriteriaRange;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

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
        var movementDate = info.getDate();
        boolean afterStart = criteria.getStartDate() == null || !movementDate.isBefore(criteria.getStartDate());
        boolean beforeEnd = criteria.getEndDate() == null || !movementDate.isAfter(criteria.getEndDate());

        return afterStart && beforeEnd;
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
                .filter(info -> info.getEntId().equals(book.getEntId())) //Filter all the accounting info of the enterprise
                .filter(info -> isWithinDateRange(info, criteria)) // Filter by date range
                .filter(info -> !criteria.hasRange() || matchesCriteria(info, criteria)) // Filter by criteria range if exists
                .filter(info -> {
                    if (criteria.getThirdPartyId() == null) return true;   // no hay filtro → pasa todo
                    if (info.getThirdPartyId() == null) return false;      // sin tercero en data → descartar

                    return info.getThirdPartyId().trim().equals(criteria.getThirdPartyId().trim());
                }) // Filter by third party id if is necessary
                .filter(info -> criteria.getCostCenterId() == null|| info.getCostCenter().getCode() == null || info.getCostCenter().getCode().equals(criteria.getCostCenterId())) // Filter by cost center code if is necessary
                .filter(info -> criteria.getVoucherType() == null || info.getVoucher().getType().equals(criteria.getVoucherType())) // Filter by voucher type if is necessary
                .toList();
    }
}