package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.controller.auxiliaryBook;

import com.unicauca.edu.co.auxiliary_book.application.ports.in.auxiliaryBook.IAuxiliaryBookCommandPort;
import com.unicauca.edu.co.auxiliary_book.application.ports.in.export.IExportReportPort;
import com.unicauca.edu.co.auxiliary_book.application.ports.in.log.IAuxiliaryBookLogQueryPort;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import com.unicauca.edu.co.auxiliary_book.domain.models.log.AuxiliaryBookLog;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.response.AuxiliaryBookHistoryResponseDTO;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.response.AuxiliaryBookResponseDTO;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.response.ResponseDTO;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.request.ExportAuxiliaryBookRequest;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.request.GenerateAuxiliaryBookRequest;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.mapper.IAuxiliaryBookRestMapper;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.mapper.IExportRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auxiliary-books")
public class AuxiliaryBookCommandController {

    private final IAuxiliaryBookCommandPort auxiliaryBookCommandPort;
    private final IAuxiliaryBookRestMapper auxiliaryBookRestMapper;

    private final IExportReportPort jasperReportGeneratorPort;
    private final IExportRestMapper exportRestMapper;

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("Auxiliary Book Command Controller is working!");
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO<AuxiliaryBookResponseDTO>> registerAuxiliaryBook(@Validated @RequestBody GenerateAuxiliaryBookRequest request) {
        AuxiliaryBook auxBookRegistered = this.auxiliaryBookCommandPort.registerAuxiliaryBook(this.auxiliaryBookRestMapper.toDomain(request));
        List<?> accountingData = this.auxiliaryBookCommandPort.genereteAuxiliaryBookInfo(auxBookRegistered);
        AuxiliaryBookResponseDTO response = new  AuxiliaryBookResponseDTO(auxBookRegistered, accountingData);
        ResponseDTO<AuxiliaryBookResponseDTO> responseDTO = ResponseDTO.<AuxiliaryBookResponseDTO>builder()
                .data(response)
                .statusCode(200)
                .message("Auxiliary Book of type "+request.getType().name()+" successfully registered!")
                .build();
        return responseDTO.of();
    }

    @PostMapping("/export")
    public ResponseEntity<byte[]> exportReport(@RequestBody ExportAuxiliaryBookRequest request) {
        byte[] reportBytes = this.jasperReportGeneratorPort.exportReport(this.exportRestMapper.toDomain(request));
        HttpHeaders headers = this.jasperReportGeneratorPort.getHttpHeaders(request.getFormat(), request.getAuxiliaryBook().getType());
        return new ResponseEntity<>(reportBytes, headers, HttpStatus.OK);
    }
}
