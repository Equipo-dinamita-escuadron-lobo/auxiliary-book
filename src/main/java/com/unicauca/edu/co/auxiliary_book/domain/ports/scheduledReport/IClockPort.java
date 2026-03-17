package com.unicauca.edu.co.auxiliary_book.domain.ports.scheduledReport;

import java.time.Instant;

public interface IClockPort {
    Instant now();
}
