package com.unicauca.edu.co.auxiliary_book.application.ports.in.log;

import com.unicauca.edu.co.auxiliary_book.domain.models.log.AuxiliaryBookLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAuxiliaryBookLogQueryPort {
    /**
     * Obtiene una página de registros de logs de libros auxiliares asociados a una entidad específica.
     *
     * @param entId el identificador de la entidad cuyos logs se desean consultar
     * @param pageable objeto que contiene la información de paginación
     * @return una página de objetos AuxiliaryBookLog correspondientes a la entidad dada
     */
    Page<AuxiliaryBookLog> findAllByEntId(String entId, Pageable pageable);
}
