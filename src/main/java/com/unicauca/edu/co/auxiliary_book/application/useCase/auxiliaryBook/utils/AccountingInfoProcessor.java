package com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.AuxiliaryBookCriteria;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.ECriteriaType;
import com.unicauca.edu.co.auxiliary_book.domain.models.external.accountingInfo.AccountingInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Service
public class AccountingInfoProcessor {

    public <T> List<T> calculateBalancesByCriteriaGroup(
            List<AccountingInfo> filteredList,
            AuxiliaryBookCriteria criteria,
            BiFunction<String, List<AccountingInfo>, T> mapper
    ) {
        return filteredList.stream()
                .collect(Collectors.groupingBy(
                        info -> extractGroupingKey(info.getAccount().getCode().toString(), criteria.getCriteriaType())
                ))
                .entrySet()
                .stream()
                .map(entry -> mapper.apply(entry.getKey(), entry.getValue()))
                .toList();
    }


    private String extractGroupingKey(String accountCode, ECriteriaType type) {
        return switch (type) {
            case NUMBER_CLASS -> accountCode.substring(0, 1);
            case GROUP -> accountCode.substring(0, Math.min(accountCode.length(), 2));
            case ACCOUNT -> accountCode.substring(0, Math.min(accountCode.length(), 4));
            case SUB_ACCOUNT -> accountCode.substring(0, Math.min(accountCode.length(), 6));
            case AUXILIARY_ACCOUNT -> accountCode.length() >= 8
                    ? accountCode.substring(0, 8)
                    : accountCode;
            default -> throw new IllegalArgumentException("Tipo de criterio no soportado para agrupación");
        };
    }


}
