package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.adapter.AuxiliaryBook;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import com.unicauca.edu.co.auxiliary_book.domain.ports.AuxiliaryBook.IAuxiliaryBookCommandRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.AuxiliaryBookEntity;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.mapper.IAuxiliaryBookCommandEntityMapper;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.repository.IAuxiliaryBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuxiliaryBookCommandAdapter implements IAuxiliaryBookCommandRepositoryPort {

    private final IAuxiliaryBookCommandEntityMapper auxiliaryBookCommandEntityMapper;
    private final IAuxiliaryBookRepository auxiliaryBookRepository;

    @Override
    public AuxiliaryBook registerAuxiliaryBook(AuxiliaryBook auxiliaryBook) {

        AuxiliaryBookEntity abEntity = this.auxiliaryBookCommandEntityMapper.toEntity(auxiliaryBook);

        return this.auxiliaryBookCommandEntityMapper.toDomain(this.auxiliaryBookRepository.save(abEntity));
    }
}
