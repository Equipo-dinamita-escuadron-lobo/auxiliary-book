package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
@Table(name = "AUXILIARY_BOOK_CRITERIA")
public class AuxiliaryBookCriteriaEntity {
    //TO DO: Cambiar las columnas y actualizar

    //Table Columns
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private int numberClass;

    @Column
    private int group;

    @Column
    private Long account;

    @Column
    private Long subAccount;

    @Column
    private Long auxiliaryAccount;

    @Column
    private Long thirdPartyId;

    @Column
    private Long costCenterId;

    @Column
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    //Relationships
    // Auxiliary Book Criteria --> Auxiliary Book
    @OneToOne(mappedBy = "criteria")
    private AuxiliaryBookEntity auxiliaryBook;
}
