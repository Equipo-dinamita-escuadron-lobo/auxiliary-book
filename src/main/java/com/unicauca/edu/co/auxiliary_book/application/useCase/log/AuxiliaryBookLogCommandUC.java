package com.unicauca.edu.co.auxiliary_book.application.useCase.log;

import com.unicauca.edu.co.auxiliary_book.application.ports.in.log.IAuxiliaryBookLogCommandPort;
import com.unicauca.edu.co.auxiliary_book.domain.models.log.AuxiliaryBookLog;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookLog.IAuxiliaryBookLogCommandRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @brief Caso de uso de escritura para los logs de Libros Auxiliares.
 *
 * Implementa el puerto de entrada {@link IAuxiliaryBookLogCommandPort}
 * y delega en el repositorio el registro de eventos (generación,
 * exportación, errores) asociados a cada libro auxiliar.
 */
@Service
@RequiredArgsConstructor
public class AuxiliaryBookLogCommandUC implements IAuxiliaryBookLogCommandPort {

    private final IAuxiliaryBookLogCommandRepositoryPort abLogCommandRepositoryPort;

    @Override
    public AuxiliaryBookLog registerAuxiliaryBookLog(AuxiliaryBookLog auxiliaryBookLog) {
        return this.abLogCommandRepositoryPort.registerAuxiliaryBookLog(auxiliaryBookLog);
    }
}
