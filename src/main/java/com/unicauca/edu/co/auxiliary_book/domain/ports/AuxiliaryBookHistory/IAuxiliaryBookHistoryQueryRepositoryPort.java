package com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBookHistory;

import com.unicauca.edu.co.auxiliary_book.domain.models.history.AuxiliaryBookHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAuxiliaryBookHistoryQueryRepositoryPort {
    /**
     * Obtiene una página de registros de historial de libros auxiliares asociados a una entidad específica.
     *
     * @param entId el identificador de la entidad cuyos historiales se desean consultar
     * @param pageable objeto que contiene la información de paginación
     * @return una página de objetos AuxiliaryBookHistory correspondientes a la entidad dada
     */
    Page<AuxiliaryBookHistory> findAllByEntId(String entId, Pageable pageable);
}
