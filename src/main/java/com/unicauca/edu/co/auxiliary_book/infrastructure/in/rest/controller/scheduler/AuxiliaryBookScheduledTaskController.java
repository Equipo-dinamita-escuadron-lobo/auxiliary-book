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
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.request.ScheduleAuxiliaryBookGenerationRequest;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.response.ResponseDTO;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.response.ScheduledTaskResponseDTO;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.mapper.AuxiliaryBookScheduledTaskMapper;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.mapper.ScheduledTaskRestMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/auxiliary-books/scheduled")
public class AuxiliaryBookScheduledTaskController {

    private final IScheduledTaskCommandPort scheduledTaskCommandPort;
    private final AuxiliaryBookScheduledTaskMapper auxiliaryBookScheduledTaskMapper;
    private final ScheduledTaskRestMapper scheduledTaskRestMapper;

    @PostMapping
    public ResponseEntity<ResponseDTO<ScheduledTaskResponseDTO>> scheduleAuxiliaryBook(
            @Valid @RequestBody ScheduleAuxiliaryBookGenerationRequest request) {

        ScheduledTask scheduledTask = this.auxiliaryBookScheduledTaskMapper.toScheduledTask(request);
        ScheduledTask created = this.scheduledTaskCommandPort.scheduleTask(scheduledTask);
        ScheduledTaskResponseDTO responseDTO = this.scheduledTaskRestMapper.toResponse(created);
        ResponseDTO<ScheduledTaskResponseDTO> response = ResponseDTO.<ScheduledTaskResponseDTO>builder()
                .data(responseDTO)
                .statusCode(HttpStatus.CREATED.value())
                .message("Auxiliary book generation scheduled successfully")
                .build();
        return response.of();
    }
}
