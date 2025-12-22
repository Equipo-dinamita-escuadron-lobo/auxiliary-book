package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity;

import com.unicauca.edu.co.auxiliary_book.domain.models.log.ETypeEvent;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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
     * @brief Public identifier for the log entry. (External ID)
     */
    @Column(unique = true, nullable = false, updatable = false)
    private String publicId;

    /**
     * @brief Type of log event.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ETypeEvent ETypeEvent;

    @Column(name = "message")
    private String message;

    /**
     * @brief Timestamp when the auxiliary book was created.
     */
    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    // Relationships
    /**
     * @brief Reference to the associated auxiliary book entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auxiliaryBookId", referencedColumnName = "id")
    private AuxiliaryBookEntity auxiliaryBook;
}
