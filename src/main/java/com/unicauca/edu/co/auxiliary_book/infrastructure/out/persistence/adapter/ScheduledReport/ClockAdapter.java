package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.adapter.ScheduledReport;

import com.unicauca.edu.co.auxiliary_book.domain.ports.scheduledReport.IClockPort;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;

@Component
@Slf4j
public class ClockAdapter implements IClockPort {

    private final Clock clock = Clock.systemDefaultZone();

    @PostConstruct
    void logClockConfiguration() {
        log.info("[ClockAdapter] Initialized clock with zone={}", clock.getZone());
    }

    @Override
    public Instant now() {
        return Instant.now(clock);
    }
}
