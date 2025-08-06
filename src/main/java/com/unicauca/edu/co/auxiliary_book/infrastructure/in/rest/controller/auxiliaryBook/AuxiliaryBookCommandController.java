package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.controller.auxiliaryBook;

import com.unicauca.edu.co.auxiliary_book.application.ports.in.auxiliaryBook.IAuxiliaryBookCommandPort;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.ResponseDTO;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.request.GenerateAuxiliaryBookRequest;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.mapper.IAuxiliaryBookResponseMapper;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.mapper.IAuxiliaryBookRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auxiliary-books")
public class AuxiliaryBookCommandController {

    private final IAuxiliaryBookCommandPort auxiliaryBookCommandPort;
    private final IAuxiliaryBookRestMapper auxiliaryBookRestMapper;
    private final IAuxiliaryBookResponseMapper auxiliaryBookResponseMapper;

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        // This is a placeholder for testing purposes.
        // You can implement your logic here to test the controller.
        return ResponseEntity.ok("Auxiliary Book Command Controller is working!");
    }

    @PostMapping("/register")
    public ResponseDTO<List<?>> registerAuxiliaryBook(@RequestBody GenerateAuxiliaryBookRequest request) {
        AuxiliaryBook response = this.auxiliaryBookCommandPort.registerAuxiliaryBook(this.auxiliaryBookRestMapper.toDomain(request));
        List<?> result = this.auxiliaryBookCommandPort.genereteAuxiliaryBookInfo(response);
        return ResponseDTO.<List<?>>builder()
                .data(result)
                .statusCode(200)
                .message("Auxiliary Book of type "+request.getType().name()+" registered successfully!")
                .build();
    }

}
