package com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook;

import java.util.List;
import java.util.UUID;

import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EState;
import com.unicauca.edu.co.auxiliary_book.domain.models.history.AuxiliaryBookHistory;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookHistory.IAuxiliaryBookHistoryCommandRepositoryPort;
import org.springframework.stereotype.Component;

import com.unicauca.edu.co.auxiliary_book.application.ports.in.auxiliaryBook.IAuxiliaryBookCommandPort;
import com.unicauca.edu.co.auxiliary_book.application.ports.out.IAccountingInfoClient;
import com.unicauca.edu.co.auxiliary_book.application.useCase.auxiliaryBook.utils.AuxiliaryBookProcessor;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import com.unicauca.edu.co.auxiliary_book.domain.models.log.AuxiliaryBookLog;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.ETypeEvent;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBook.IAuxiliaryBookCommandRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookLog.IAuxiliaryBookLogCommandRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.IFormatterResultOutputPort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.IMessageServicePort;
import com.unicauca.edu.co.auxiliary_book.infrastructure.config.i18n.MessageKeys;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuxiliaryBookCommandUC implements IAuxiliaryBookCommandPort {

    private final IAuxiliaryBookCommandRepositoryPort abCommandRepositoryPort;
    private final IAuxiliaryBookLogCommandRepositoryPort abLogCommandRepositoryPort;
    private final IAuxiliaryBookHistoryCommandRepositoryPort abHistoryCommandRepositoryPort;

    private final IAccountingInfoClient accountingInfoQueryPort;
    private final AuxiliaryBookProcessor auxiliaryBookProcessor;

    private final IFormatterResultOutputPort formatterResultOutputPort;
    private final IMessageServicePort messageServicePort;

    @Override
    public AuxiliaryBook registerAuxiliaryBook(AuxiliaryBook auxiliaryBook) {

        if(auxiliaryBook == null){
            this.formatterResultOutputPort.returnErrorGenericResponse(400, this.messageServicePort.getMessage(
                    MessageKeys.ERROR_NULL_VALUE,
                    "AuxiliaryBook"
            ));
        }

        // --- 3. Lógica de UUID (Capa de Aplicación) ---
        // Se asigna el ID público ANTES de guardarlo.
        if (auxiliaryBook.getPublicId() == null || auxiliaryBook.getPublicId().isEmpty()) {
            auxiliaryBook.setPublicId(UUID.randomUUID().toString());
        }

        // 4. Guardar el libro (ahora con publicId)
        AuxiliaryBook abRegistered = abCommandRepositoryPort.registerAuxiliaryBook(auxiliaryBook);

        AuxiliaryBookHistory newHistory = AuxiliaryBookHistory.builder()
                .publicId(UUID.randomUUID().toString())
                .auxiliaryBook(abRegistered)
                .state(EState.GENERATED)
                .build();

        this.abHistoryCommandRepositoryPort.registerAuxiliaryBookHistory(newHistory);

        // 5. Crear el log de registro
        String logMessage = "Auxiliary book registered successfully.";
        this.createLog(abRegistered, ETypeEvent.REGISTERED, logMessage);

        return abRegistered;
    }

    @Override
    public List<?> genereteAuxiliaryBookInfo(AuxiliaryBook auxiliaryBook) {

        // 6. Log de INICIO de generación
        log.info("Starting generation for book: {}", auxiliaryBook.getPublicId());
        this.createLog(auxiliaryBook, ETypeEvent.GENERATING, "Starting data generation process.");

        try {
            // 7. Ejecutar el proceso
            List<?> resultData = this.auxiliaryBookProcessor.processAuxiliaryBookData(this.accountingInfoQueryPort, auxiliaryBook);

            // 8. Log de ÉXITO
            String successMessage = "Data generation successful. " + (resultData != null ? resultData.size() : 0) + " items processed.";
            this.createLog(auxiliaryBook, ETypeEvent.SUCCESS_GENERATION, successMessage);

            return resultData;

        } catch (Exception e) {
            // 9. Log de ERROR
            log.error("Error during data generation for book: {}", auxiliaryBook.getPublicId(), e);
            String errorMessage = "Error during generation: " + e.getMessage();
            this.createLog(auxiliaryBook, ETypeEvent.ERROR_GENERATION, errorMessage);

            // 10. Lanzar la respuesta de error (como ya lo hacías)
            this.formatterResultOutputPort.returnErrorGenericResponse(500, this.messageServicePort.getMessage(
                    MessageKeys.ERROR_GENERIC, // O una clave de error más específica
                    errorMessage
            ));

            // returnErrorGenericResponse seguramente lanza una excepción, así que esto es por si acaso.
            return null;
        }
    }

    /**
     * Método helper privado para estandarizar la creación de logs.
     * Cada entrada de log tendrá su propio publicId único.
     */
    private void createLog(AuxiliaryBook book, ETypeEvent event, String message) {
        AuxiliaryBookLog newLog = AuxiliaryBookLog.builder()
                .publicId(UUID.randomUUID().toString()) // <-- Asigna UUID al Log
                .auxiliaryBook(book)
                .ETypeEvent(event)
                .message(message)
                .build();

        abLogCommandRepositoryPort.registerAuxiliaryBookLog(newLog);
    }
}