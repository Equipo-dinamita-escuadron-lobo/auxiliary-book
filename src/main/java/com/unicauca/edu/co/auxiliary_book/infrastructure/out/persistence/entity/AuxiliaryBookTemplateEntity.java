package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity;

import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAlignment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @brief Entity representing a template for an auxiliary book.
 *
 * Maps template configuration such as name, logo path, alignment, font, and color,
 * and links to the associated auxiliary book entity.
 */
@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
@Table(name = "AUXILIARY_BOOK_TEMPLATE")
public class AuxiliaryBookTemplateEntity {

    // Table Columns

    /**
     * @brief Unique identifier for the auxiliary book template.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @brief Name of the template.
     */
    @Column(nullable = false)
    private String name;

    /**
     * @brief Path to the logotype image for the template.
     */
    @Column(nullable = false)
    private String pathLogotype;

    /**
     * @brief Alignment setting for the template.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EAlignment aligment;

    /**
     * @brief Font used in the template.
     */
    @Column(nullable = false)
    private String font;

    /**
     * @brief Main color used in the template.
     */
    @Column(nullable = false)
    private String mainColor;

    // Relationships

    /**
     * @brief Reference to the associated auxiliary book entity.
     */
    @OneToOne(mappedBy = "template")
    private AuxiliaryBookEntity auxiliaryBook;
}
