package com.unicauca.edu.co.auxiliary_book.integration.controller;

import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.repository.IAuxiliaryBookRepository;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the Kardex purchase endpoint
 * Tests focus on validating proper request handling, validation,
 * and response structure for the kardex purchase operation.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuxiliaryBookCommandController {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IAuxiliaryBookRepository auxiliaryBookRepository;


    private static final String BASE_URL = "/api/kardex/weighted-average/purchase";
}
