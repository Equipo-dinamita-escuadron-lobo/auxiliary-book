package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "AUXILIARY_BOOK_LOG")
public class AuxiliaryBookLogEntity {
    //Table Columns
    @Id
    @SequenceGenerator(name = "aux_book_log_seq", sequenceName = "auxiliary_book_log_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aux_book_log_seq")
    private Long id;


    @Column(nullable = false)
    private String logTypeEvent;

    //Relationships
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "auxiliaryBookId", referencedColumnName = "id")
    private AuxiliaryBookEntity auxiliaryBook;
}
