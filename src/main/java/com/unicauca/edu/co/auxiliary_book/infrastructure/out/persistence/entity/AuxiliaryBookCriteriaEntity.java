package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity;

import com.unicauca.edu.co.auxiliary_book.domain.models.enums.ECriteriaType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @brief Entity representing the criteria for an auxiliary book.
 *
 * Maps the criteria used to filter or generate auxiliary book data,
 * including type, range, third party, cost center, and date range.
 */
@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
@Table(name = "AUXILIARY_BOOK_CRITERIA")
public class AuxiliaryBookCriteriaEntity {
    // Table Columns

    /**
     * @brief Unique identifier for the criteria entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * @brief Type of criteria applied to the auxiliary book.
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ECriteriaType criteriaType;

    /**
     * @brief Start of the range for the criteria (if applicable).
     */
    @Column
    private Long fromRange;

    /**
     * @brief End of the range for the criteria (if applicable).
     */
    @Column
    private Long toRange;

    /**
     * @brief Identifier for the third party associated with the criteria.
     */
    @Column
    private String thirdPartyId;

    /**
     * @brief Identifier for the cost center associated with the criteria.
     */
    @Column
    private String costCenterId;

    /**
     * @brief Start date for the criteria's validity.
     */
    @Column
    private LocalDate startDate;

    /**
     * @brief End date for the criteria's validity.
     */
    @Column(nullable = false)
    private LocalDate endDate;

    // Relationships

    /**
     * @brief Reference to the associated auxiliary book entity.
     */
    @OneToOne(mappedBy = "criteria")
    private AuxiliaryBookEntity auxiliaryBook;
}
