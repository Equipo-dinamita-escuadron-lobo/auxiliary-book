package com.unicauca.edu.co.auxiliary_book.domain.models.core.validators;

import jdk.jfr.Frequency;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleValidator {
    private Set<Frequency> supportedFrequencies;
}
