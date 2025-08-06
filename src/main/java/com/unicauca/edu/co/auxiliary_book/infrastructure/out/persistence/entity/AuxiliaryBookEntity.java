package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity;

import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookFormat;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
@Table(name = "AUXILIARY_BOOK")
public class AuxiliaryBookEntity {

    //Table Columns
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EAuxiliaryBookType type;

    @Column(nullable = false)
    private String entId;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EAuxiliaryBookFormat format;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    //Relationships
    // Auxiliary Book -->  Criteria
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "abTemplateId", referencedColumnName = "id")
    private AuxiliaryBookTemplateEntity template;

    // Auxiliary Book -->  Criteria
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "abCriteriaId", referencedColumnName = "id")
    private AuxiliaryBookCriteriaEntity criteria;

    // Reference to the log entity
    @OneToOne(mappedBy = "auxiliaryBook", cascade = CascadeType.ALL)
    private AuxiliaryBookLogEntity log;

    // Reference to the history entity
    @OneToOne(mappedBy = "auxiliaryBook", cascade = CascadeType.ALL)
    private AuxiliaryBookHistoryEntity history;

}
