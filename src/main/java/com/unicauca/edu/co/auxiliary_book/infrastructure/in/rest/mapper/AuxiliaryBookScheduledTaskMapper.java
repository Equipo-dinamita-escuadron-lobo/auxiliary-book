package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.mapper;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EDeliveryWay;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTask;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTaskType;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.TaskExecutionTime;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.payload.AuxiliaryBookScheduledPayload;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.request.ScheduleAuxiliaryBookGenerationRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuxiliaryBookScheduledTaskMapper {

    private final ObjectMapper objectMapper;
    private final IAuxiliaryBookRestMapper auxiliaryBookRestMapper;

    public ScheduledTask toScheduledTask(ScheduleAuxiliaryBookGenerationRequest request) {
        AuxiliaryBook auxiliaryBook = this.auxiliaryBookRestMapper.toDomain(request.auxiliaryBook());
        auxiliaryBook.changeFormat(request.format());

        AuxiliaryBookScheduledPayload payload = AuxiliaryBookScheduledPayload.builder()
                .deliveryWay(request.deliveryWay())
                .emailTo(request.emailTo())
                .emailSubject(request.emailSubject())
                .emailBody(request.emailBody())
                .format(request.format())
                .entName(request.entName())
                .auxiliaryBook(auxiliaryBook)
                .template(request.template())
                .build();

        Map<String, Object> payloadMap = this.objectMapper.convertValue(
                payload,
                new TypeReference<Map<String, Object>>() {
                });

        ScheduledTaskType taskType = request.deliveryWay() == EDeliveryWay.EMAIL
                ? ScheduledTaskType.AUXILIARY_BOOK_EMAIL
                : ScheduledTaskType.AUXILIARY_BOOK_DOWNLOAD;

        ScheduledTask scheduledTask = new ScheduledTask();
        scheduledTask.setTaskType(taskType);
        scheduledTask.setExecuteAt(TaskExecutionTime.from(request.executeAt().toInstant()));
        scheduledTask.setPayload(payloadMap);
        return scheduledTask;
    }
}
