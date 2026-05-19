package com.unicauca.edu.co.auxiliary_book.copy.infrastructure.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.unicauca.edu.co.auxiliary_book.copy.application.input.*;
import com.unicauca.edu.co.auxiliary_book.copy.infrastructure.in.rest.controller.AuxBookCopyController;
import com.unicauca.edu.co.auxiliary_book.copy.infrastructure.in.rest.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests del controlador de copia de auxiliary-book.
 * Standalone MockMvc — sin contexto Spring completo.
 * RED phase: Fase 9.5.
 */
@ExtendWith(MockitoExtension.class)
class AuxBookCopyControllerTest {

    @Mock private IExecuteAuxBookCopyPhasePort executePort;
    @Mock private IGetAuxBookCopyStatusPort statusPort;
    @Mock private ICancelAuxBookCopyPort cancelPort;
    @Mock private ICleanupAuxBookCopyPort cleanupPort;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        AuxBookCopyController controller =
                new AuxBookCopyController(executePort, statusPort, cancelPort, cleanupPort);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("POST /api/auxiliary-books/copy/phase retorna 200 con COMPLETADO")
    void executePhase_retorna200() throws Exception {
        CopyPhaseRequestDto request = CopyPhaseRequestDto.builder()
                .idProceso(UUID.randomUUID())
                .fase(1)
                .entOrigen("ENT_A")
                .entDestino("ENT_B")
                .snapshotCorte(Instant.now())
                .equivalenciasPrev(List.of())
                .build();

        CopyPhaseResponseDto response = CopyPhaseResponseDto.builder()
                .estado("COMPLETADO")
                .registrosProcesados(2)
                .equivalenciasGeneradas(List.of())
                .mensaje("Copia auxbook completada")
                .advertencias(List.of())
                .build();

        when(executePort.ejecutar(any())).thenReturn(response);

        mockMvc.perform(post("/api/auxiliary-books/copy/phase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("COMPLETADO"));
    }

    @Test
    @DisplayName("GET /api/auxiliary-books/copy/{id}/status retorna 200")
    void getStatus_retorna200() throws Exception {
        String idProceso = UUID.randomUUID().toString();
        CopyStatusResponseDto response = CopyStatusResponseDto.builder()
                .fase(1).estado("COMPLETADO").registrosProcesados(3).intentos(1).build();
        when(statusPort.obtenerEstado(idProceso)).thenReturn(response);

        mockMvc.perform(get("/api/auxiliary-books/copy/{idProceso}/status", idProceso))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("COMPLETADO"));
    }

    @Test
    @DisplayName("POST /api/auxiliary-books/copy/{id}/cancel retorna 200 con CANCELADO")
    void cancel_retorna200() throws Exception {
        String idProceso = UUID.randomUUID().toString();
        CopyCancelResponseDto response = CopyCancelResponseDto.builder()
                .estado("CANCELADO").mensaje("Proceso cancelado").build();
        when(cancelPort.cancelar(idProceso)).thenReturn(response);

        mockMvc.perform(post("/api/auxiliary-books/copy/{idProceso}/cancel", idProceso))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CANCELADO"));
    }

    @Test
    @DisplayName("DELETE /api/auxiliary-books/copy/{id}/cleanup retorna 204")
    void cleanup_retorna204() throws Exception {
        mockMvc.perform(delete("/api/auxiliary-books/copy/{idProceso}/cleanup", UUID.randomUUID().toString()))
                .andExpect(status().isNoContent());
    }
}
