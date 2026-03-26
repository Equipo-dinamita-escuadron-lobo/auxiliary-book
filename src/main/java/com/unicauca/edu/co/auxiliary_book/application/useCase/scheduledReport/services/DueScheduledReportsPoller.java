package com.unicauca.edu.co.auxiliary_book.application.useCase.scheduledReport.services;

import com.unicauca.edu.co.auxiliary_book.application.ports.in.scheduledReport.IRunDueScheduledReportPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DueScheduledReportsPoller {

    private final IRunDueScheduledReportPort runDueScheduledReportPort;

    public void poll() {
        this.runDueScheduledReportPort.runDueScheduledReports();
    }
}
