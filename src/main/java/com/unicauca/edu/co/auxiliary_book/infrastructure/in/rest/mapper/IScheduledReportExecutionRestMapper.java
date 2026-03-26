package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.mapper;

import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledJobExecution.ReportExecution;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.response.scheduledReport.ScheduledReportExecutionListItemResponse;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.response.scheduledReport.ScheduledReportExecutionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IScheduledReportExecutionRestMapper {

    default ScheduledReportExecutionResponse toResponse(ReportExecution execution) {
        if (execution == null) {
            return null;
        }
        ScheduledReportExecutionResponse response = new ScheduledReportExecutionResponse();
        response.setExecutionId(execution.getExecutionId());
        response.setJobId(execution.getJobId());
        response.setScheduledAt(execution.getScheduledAt());
        response.setStartedAt(execution.getStartedAt());
        response.setFinishedAt(execution.getFinishedAt());
        response.setStatusExecution(execution.getStatusExecution());
        response.setDeliveryStatus(execution.getDeliveryStatus());
        response.setErrorMessage(execution.getErrorMessage());
        return response;
    }

    default ScheduledReportExecutionListItemResponse toListItemResponse(ReportExecution execution) {
        if (execution == null) {
            return null;
        }
        ScheduledReportExecutionListItemResponse response = new ScheduledReportExecutionListItemResponse();
        response.setExecutionId(execution.getExecutionId());
        response.setScheduledAt(execution.getScheduledAt());
        response.setStatusExecution(execution.getStatusExecution());
        response.setDeliveryStatus(execution.getDeliveryStatus());
        response.setErrorMessage(execution.getErrorMessage());
        return response;
    }
}
