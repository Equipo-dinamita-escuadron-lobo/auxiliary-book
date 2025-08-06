package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity;

import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAlignment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
@Table(name = "AUXILIARY_BOOK_TEMPLATE")
public class AuxiliaryBookTemplateEntity {

    //Table Columns
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String PathLogotype;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EAlignment aligment;

    @Column(nullable = false)
    private String font;

    @Column(nullable = false)
    private String mainColor;

    //Relationships
    @OneToOne(mappedBy = "template")
    private AuxiliaryBookEntity auxiliaryBook;
}
