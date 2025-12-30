package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.controller.scheduler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unicauca.edu.co.auxiliary_book.application.ports.in.scheduledTasks.IScheduledTaskCommandPort;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTask;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.request.ScheduleTaskRequest;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.response.ResponseDTO;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.response.ScheduledTaskResponseDTO;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.mapper.ScheduledTaskRestMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/scheduled-tasks")
public class ScheduledTaskCommandController {

    private final IScheduledTaskCommandPort scheduledTaskCommandPort;
    private final ScheduledTaskRestMapper scheduledTaskRestMapper;

    @PostMapping
    public ResponseEntity<ResponseDTO<ScheduledTaskResponseDTO>> scheduleTask(
            @Valid @RequestBody ScheduleTaskRequest request) {
        ScheduledTask scheduledTask = this.scheduledTaskRestMapper.toDomain(request);
        ScheduledTask createdTask = this.scheduledTaskCommandPort.scheduleTask(scheduledTask);
        ScheduledTaskResponseDTO response = this.scheduledTaskRestMapper.toResponse(createdTask);
        ResponseDTO<ScheduledTaskResponseDTO> wrapper = ResponseDTO.<ScheduledTaskResponseDTO>builder()
                .data(response)
                .statusCode(HttpStatus.CREATED.value())
                .message("Scheduled task created successfully")
                .build();
        return wrapper.of();
    }
}
