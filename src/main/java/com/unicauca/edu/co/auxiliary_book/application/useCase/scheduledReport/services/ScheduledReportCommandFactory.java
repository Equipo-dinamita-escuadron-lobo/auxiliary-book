package com.unicauca.edu.co.auxiliary_book.application.useCase.scheduledReport.services;

import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EFrequency;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.stereotype.Component;

@Component
public class ScheduledReportCommandFactory {

    public Instant computeNextRunAt(Instant base, EFrequency frequency) {
        if (base == null || frequency == null) {
            return base;
        }
        ZonedDateTime zoned = base.atZone(ZoneId.systemDefault());
        return switch (frequency) {
            case DAILY -> zoned.plusDays(1).toInstant();
            case WEEKLY -> zoned.plusWeeks(1).toInstant();
            case MONTHLY -> zoned.plusMonths(1).toInstant();
        };
    }

    public Instant computeFirstRunAfter(Instant startAt, EFrequency frequency, Instant now) {
        if (startAt == null) {
            return null;
        }
        if (frequency == null || now == null) {
            return startAt;
        }
        Instant candidate = startAt;
        while (candidate.isBefore(now)) {
            candidate = computeNextRunAt(candidate, frequency);
        }
        return candidate;
    }
}
