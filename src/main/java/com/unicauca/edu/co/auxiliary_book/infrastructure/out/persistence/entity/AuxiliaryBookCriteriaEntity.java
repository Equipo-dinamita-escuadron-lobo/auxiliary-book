package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity;

import com.unicauca.edu.co.auxiliary_book.domain.models.enums.ECriteriaType;
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
    //Table Columns
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ECriteriaType criteriaType;

    @Column
    private Long fromRange;

    @Column
    private Long toRange;

    @Column
    private String thirdPartyId;

    @Column
    private String costCenterId;

    @Column
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    //Relationships
    // Auxiliary Book Criteria --> Auxiliary Book
    @OneToOne(mappedBy = "criteria")
    private AuxiliaryBookEntity auxiliaryBook;
}
