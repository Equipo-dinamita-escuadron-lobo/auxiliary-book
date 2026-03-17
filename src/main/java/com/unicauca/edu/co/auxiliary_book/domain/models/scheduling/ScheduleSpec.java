package com.unicauca.edu.co.auxiliary_book.domain.models.scheduling;

import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EFrequency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleSpec {
    private EFrequency frequency;
    private Instant startAt;
    private Optional<Instant> endAt;
    private Instant nextRunAt;
}
