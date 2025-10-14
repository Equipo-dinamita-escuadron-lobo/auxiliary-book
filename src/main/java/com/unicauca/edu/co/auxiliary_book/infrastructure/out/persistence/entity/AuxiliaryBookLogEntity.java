package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @brief Entity representing a log entry for an auxiliary book.
 *
 * Maps log events related to an auxiliary book and links to the associated book entity.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "AUXILIARY_BOOK_LOG")
public class AuxiliaryBookLogEntity {
    // Table Columns

    /**
     * @brief Unique identifier for the auxiliary book log entry.
     */
    @Id
    @SequenceGenerator(name = "aux_book_log_seq", sequenceName = "auxiliary_book_log_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aux_book_log_seq")
    private Long id;

    /**
     * @brief Type of log event.
     */
    @Column(nullable = false)
    private String logTypeEvent;

    // Relationships

    /**
     * @brief Reference to the associated auxiliary book entity.
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "auxiliaryBookId", referencedColumnName = "id")
    private AuxiliaryBookEntity auxiliaryBook;
}
