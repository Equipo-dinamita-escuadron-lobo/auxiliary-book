package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.mapper;

import java.net.MalformedURLException;
import java.net.URL;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.AuxiliaryBookCriteria;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.CriteriaRange;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.export.AuxiliaryBookTemplate;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookCriteriaEntity;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookEntity;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookTemplateEntity;

@Mapper(componentModel = "spring")
public interface IAuxiliaryBookCommandEntityMapper {

    @Mapping(target = "log", ignore = true)
    @Mapping(target = "history", ignore = true)
    @Mapping(target = "criteria.auxiliaryBook", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(source = "template", target = "template")
    @Mapping(source = "criteria", target = "criteria", qualifiedByName = "criteriaToEntity")
    AuxiliaryBookEntity toEntity(AuxiliaryBook auxiliaryBook);

    @Mapping(target = "criteria", source = "criteria", qualifiedByName = "criteriaEntityToDomain")
    @Mapping(target = "template", source = "template")
    @Mapping(target = "criteria.voucherType", ignore = true)
    AuxiliaryBook toDomain(AuxiliaryBookEntity auxiliaryBookEntity);

    @Named("criteriaToEntity")
    @Mapping(target = "auxiliaryBook", ignore = true)
    @Mapping(source = "criteriaRange.fromRange", target = "fromRange")
    @Mapping(source = "criteriaRange.toRange", target = "toRange")
    AuxiliaryBookCriteriaEntity toCriteriaEntity(AuxiliaryBookCriteria criteria);

    @Named("criteriaEntityToDomain")
    @Mapping(target = "criteriaRange", expression = "java(mapCriteriaRange(criteriaEntity))")
    @Mapping(target = "voucherType", ignore = true)
    AuxiliaryBookCriteria toCriteria(AuxiliaryBookCriteriaEntity criteriaEntity);

    @Mapping(target = "auxiliaryBook", ignore = true)
    @Mapping(source = "pathLogotype", target = "pathLogotype", qualifiedByName = "urlToString")
    @Mapping(source = "alienation", target = "aligment")
    AuxiliaryBookTemplateEntity toTemplateEntity(AuxiliaryBookTemplate template);

    @Mapping(source = "pathLogotype", target = "pathLogotype", qualifiedByName = "stringToUrl")
    @Mapping(source = "aligment", target = "alienation")
    @Mapping(target = "fontSize", ignore = true)
    AuxiliaryBookTemplate toTemplate(AuxiliaryBookTemplateEntity templateEntity);

    default CriteriaRange mapCriteriaRange(AuxiliaryBookCriteriaEntity criteriaEntity) {
        if (criteriaEntity == null) {
            return null;
        }
        if (criteriaEntity.getFromRange() == null && criteriaEntity.getToRange() == null) {
            return null;
        }
        return new CriteriaRange(criteriaEntity.getFromRange(), criteriaEntity.getToRange());
    }

    @Named("stringToUrl")
    default URL stringToUrl(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return new URL(value);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @Named("urlToString")
    default String urlToString(URL url) {
        return url != null ? url.toString() : null;
    }
}
