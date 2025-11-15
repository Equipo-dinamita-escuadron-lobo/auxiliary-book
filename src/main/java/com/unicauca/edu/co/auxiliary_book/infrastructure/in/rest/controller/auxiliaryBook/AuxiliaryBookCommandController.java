package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.controller.auxiliaryBook;

import com.unicauca.edu.co.auxiliary_book.application.ports.in.auxiliaryBook.IAuxiliaryBookCommandPort;
import com.unicauca.edu.co.auxiliary_book.application.ports.in.export.IExportReportPort;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.ResponseDTO;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.request.ExportAuxiliaryBookRequest;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.request.GenerateAuxiliaryBookRequest;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.mapper.IAuxiliaryBookRestMapper;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.mapper.IExportRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseDTO<List<?>>> registerAuxiliaryBook(@RequestBody GenerateAuxiliaryBookRequest request) {
        AuxiliaryBook response = this.auxiliaryBookCommandPort.registerAuxiliaryBook(this.auxiliaryBookRestMapper.toDomain(request));
        List<?> result = this.auxiliaryBookCommandPort.genereteAuxiliaryBookInfo(response);
        ResponseDTO<List<?>> responseDTO = ResponseDTO.<List<?>>builder()
                .data(result)
                .statusCode(200)
                .message("Auxiliary Book of type "+request.getType().name()+" registered successfully!")
                .build();
        return responseDTO.of();
    }

    @PostMapping("/export")
    public ResponseEntity<byte[]> exportReport(@RequestBody ExportAuxiliaryBookRequest request) {
        byte[] reportBytes = this.jasperReportGeneratorPort.exportReport(this.exportRestMapper.toDomain(request));
        HttpHeaders headers = this.jasperReportGeneratorPort.getHttpHeaders(request.getFormat(), request.getAuxBookType());
        return new ResponseEntity<>(reportBytes, headers, HttpStatus.OK);
    }
}
