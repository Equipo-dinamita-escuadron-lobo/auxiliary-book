package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity;

import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EDeliveryWay;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @brief Entity representing the history of an auxiliary book.
 *
 * Maps the state and delivery information for an auxiliary book's history,
 * and links to the associated auxiliary book entity.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "AUXILIARY_BOOK_HISTORY")
public class AuxiliaryBookHistoryEntity {
    /**
     * @brief Unique identifier for the auxiliary book history.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * @brief Public identifier for the auxiliary book. (External ID)
     */
    @Column(unique = true, nullable = false, updatable = false)
    private String publicId;


    /**
     * @brief State of the auxiliary book history.
     */
    @Column(nullable = false)
    private EState state;

    /**
     * @brief Delivery way for the auxiliary book history.
     */
    @Enumerated(EnumType.STRING)
    private EDeliveryWay deliveryWay;

    // Relationships

    /**
     * @brief Reference to the associated auxiliary book entity.
     */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "auxiliary_book_id", nullable = false)
    private AuxiliaryBookEntity auxiliaryBook;
}
