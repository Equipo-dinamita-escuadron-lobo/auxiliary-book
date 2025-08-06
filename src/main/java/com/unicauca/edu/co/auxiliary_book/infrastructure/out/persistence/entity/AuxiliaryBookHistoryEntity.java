package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity;

import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EDeliveryWay;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "AUXILIARY_BOOK_HISTORY")
public class AuxiliaryBookHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private EState state;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EDeliveryWay deliveryWay;

    //Relationships
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private AuxiliaryBookEntity auxiliaryBook;
}
