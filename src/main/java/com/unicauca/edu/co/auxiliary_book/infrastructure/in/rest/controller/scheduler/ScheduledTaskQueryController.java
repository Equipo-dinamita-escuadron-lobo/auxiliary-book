package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.controller.scheduler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unicauca.edu.co.auxiliary_book.application.ports.in.scheduledTasks.IScheduledTaskQueryPort;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTask;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTaskStatus;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTaskType;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.response.ResponseDTO;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.response.ScheduledTaskResponseDTO;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.mapper.ScheduledTaskRestMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scheduled-tasks")
public class ScheduledTaskQueryController {

    private final IScheduledTaskQueryPort scheduledTaskQueryPort;
    private final ScheduledTaskRestMapper scheduledTaskRestMapper;

    @GetMapping
    public ResponseEntity<ResponseDTO<Page<ScheduledTaskResponseDTO>>> listScheduledTasks(
            @RequestParam(name = "status", required = false) ScheduledTaskStatus status,
            @RequestParam(name = "type", required = false) ScheduledTaskType type,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "executeAt"));
        Page<ScheduledTaskResponseDTO> dtoPage = this.scheduledTaskQueryPort.findByFilters(status, type, pageable)
                .map(this.scheduledTaskRestMapper::toResponse);
        ResponseDTO<Page<ScheduledTaskResponseDTO>> response = ResponseDTO.<Page<ScheduledTaskResponseDTO>>builder()
                .data(dtoPage)
                .statusCode(HttpStatus.OK.value())
                .message("Scheduled tasks retrieved successfully")
                .build();
        return response.of();
    }

    @GetMapping("/{publicId}")
    public ResponseEntity<ResponseDTO<ScheduledTaskResponseDTO>> getTask(@PathVariable String publicId) {
        ScheduledTask task = this.scheduledTaskQueryPort.findByPublicId(publicId);
        ScheduledTaskResponseDTO responseDTO = this.scheduledTaskRestMapper.toResponse(task);
        ResponseDTO<ScheduledTaskResponseDTO> response = ResponseDTO.<ScheduledTaskResponseDTO>builder()
                .data(responseDTO)
                .statusCode(HttpStatus.OK.value())
                .message("Scheduled task retrieved successfully")
                .build();
        return response.of();
    }

    @GetMapping("/{publicId}/artifact")
    public ResponseEntity<Resource> downloadArtifact(@PathVariable String publicId) throws IOException {
        ScheduledTask task = this.scheduledTaskQueryPort.findByPublicId(publicId);
        String artifactPath = task.getArtifactPath();
        if (artifactPath == null || artifactPath.isBlank()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Path filePath = Paths.get(artifactPath);
        if (!Files.exists(filePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Resource resource = new FileSystemResource(filePath);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filePath.getFileName().toString() + "\"")
                .body(resource);
    }
}
