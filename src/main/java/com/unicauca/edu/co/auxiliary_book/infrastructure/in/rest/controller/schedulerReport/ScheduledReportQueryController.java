package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.controller.schedulerReport;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unicauca.edu.co.auxiliary_book.application.ports.in.scheduledReport.IScheduledReportQueryPort;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduling.ScheduledAuxiliaryBookJob;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.response.ResponseDTO;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.response.scheduledReport.ScheduledReportListItemResponse;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.response.scheduledReport.ScheduledReportResponse;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.mapper.IScheduledReportRestMapper;

import lombok.RequiredArgsConstructor;

/**
 * @brief Controlador REST de consulta de reportes programados.
 *
 * Expone endpoints para listar reportes programados por empresa y
 * consultar el detalle de un reporte específico por su ID público,
 * adaptando los resultados del dominio a DTOs de respuesta.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auxiliary-books/scheduled-reports")
public class ScheduledReportQueryController {

    private final IScheduledReportQueryPort scheduledReportQueryPort;
    private final IScheduledReportRestMapper scheduledReportRestMapper;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<ScheduledReportListItemResponse>>> list(@RequestParam String entId) {
        List<ScheduledAuxiliaryBookJob> jobs = scheduledReportQueryPort.listScheduledReports(entId);
        List<ScheduledReportListItemResponse> responseItems = jobs.stream()
                .map(scheduledReportRestMapper::toListItemResponse)
                .toList();

        ResponseDTO<List<ScheduledReportListItemResponse>> responseDTO = ResponseDTO.<List<ScheduledReportListItemResponse>>builder()
                .data(responseItems)
                .statusCode(200)
                .message("Scheduled reports retrieved successfully.")
                .build();
        return responseDTO.of();
    }

    @GetMapping("/{publicId}")
    public ResponseEntity<ResponseDTO<ScheduledReportResponse>> get(@PathVariable String publicId) {
        ScheduledAuxiliaryBookJob job = scheduledReportQueryPort.getScheduledReport(publicId);
        ResponseDTO<ScheduledReportResponse> responseDTO = ResponseDTO.<ScheduledReportResponse>builder()
                .data(scheduledReportRestMapper.toResponse(job))
                .statusCode(200)
                .message("Scheduled report retrieved successfully.")
                .build();
        return responseDTO.of();
    }
}
