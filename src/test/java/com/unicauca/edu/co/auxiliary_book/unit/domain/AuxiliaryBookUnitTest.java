package com.unicauca.edu.co.auxiliary_book.unit.domain;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.AuxiliaryBook;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.AuxiliaryBookCriteria;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.export.AuxiliaryBookTemplate;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookFormat;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAuxiliaryBookType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class AuxiliaryBookUnitTest {

        // Mocks para las dependencias complejas
        @Mock
        private AuxiliaryBookTemplate mockTemplate;
        @Mock
        private AuxiliaryBookCriteria mockCriteria;

        private AuxiliaryBook auxiliaryBook;
        private LocalDateTime testTime;

        /**
         * Configuración inicial antes de cada prueba.
         * Crea una instancia base de AuxiliaryBook usando el builder.
         */
        @BeforeEach
        void setUp() {
            testTime = LocalDateTime.of(2025, 1, 15, 10, 30);

            auxiliaryBook = AuxiliaryBook.builder()
                    .id(1L)
                    .publicId("uuid-test-123")
                    .type(EAuxiliaryBookType.INVENTORY_AND_BALANCES) // Asumiendo que este valor existe
                    .entId("ent-001")
                    .userId(100L)
                    .format(EAuxiliaryBookFormat.PDF) // Asumiendo que este valor existe
                    .template(mockTemplate)
                    .criteria(mockCriteria)
                    .createdAt(testTime)
                    .build();
        }

        @Test
        @DisplayName("Debería crear instancia y obtener valores con Builder y Getters")
        void testBuilderAndGetters() {
            // Assert
            Assertions.assertThat(auxiliaryBook.getId()).isEqualTo(1L);
            Assertions.assertThat(auxiliaryBook.getPublicId()).isEqualTo("uuid-test-123");
            Assertions.assertThat(auxiliaryBook.getType()).isEqualTo(EAuxiliaryBookType.INVENTORY_AND_BALANCES);
            Assertions.assertThat(auxiliaryBook.getEntId()).isEqualTo("ent-001");
            Assertions.assertThat(auxiliaryBook.getUserId()).isEqualTo(100L);
            Assertions.assertThat(auxiliaryBook.getFormat()).isEqualTo(EAuxiliaryBookFormat.PDF);
            Assertions.assertThat(auxiliaryBook.getTemplate()).isSameAs(mockTemplate);
            Assertions.assertThat(auxiliaryBook.getCriteria()).isSameAs(mockCriteria);
            Assertions.assertThat(auxiliaryBook.getCreatedAt()).isEqualTo(testTime);
        }

        @Test
        @DisplayName("Debería funcionar el constructor sin argumentos")
        void testNoArgsConstructor() {
            // Act
            AuxiliaryBook book = new AuxiliaryBook();

            // Assert
            Assertions.assertThat(book).isNotNull();
            Assertions.assertThat(book.getId()).isNull();
            Assertions.assertThat(book.getPublicId()).isNull();
        }

        @Test
        @DisplayName("Debería funcionar el constructor con todos los argumentos")
        void testAllArgsConstructor() {
            // Act
            AuxiliaryBook allArgsBook = new AuxiliaryBook(
                    2L, "uuid-456", EAuxiliaryBookType.ACCOUNT, // Asumiendo valor
                    "ent-002", 200L, EAuxiliaryBookFormat.EXCEL, // Asumiendo valor
                    mockTemplate, mockCriteria, testTime
            );

            // Assert
            Assertions.assertThat(allArgsBook.getId()).isEqualTo(2L);
            Assertions.assertThat(allArgsBook.getPublicId()).isEqualTo("uuid-456");
            Assertions.assertThat(allArgsBook.getType()).isEqualTo(EAuxiliaryBookType.ACCOUNT);
            Assertions.assertThat(allArgsBook.getFormat()).isEqualTo(EAuxiliaryBookFormat.EXCEL);
            Assertions.assertThat(allArgsBook.getTemplate()).isSameAs(mockTemplate);
        }

        @Test
        @DisplayName("Debería funcionar los Setters (provistos por @Data)")
        void testSetters() {
            // Arrange
            AuxiliaryBook book = new AuxiliaryBook();
            LocalDateTime newTime = LocalDateTime.now();

            // Act
            book.setId(3L);
            book.setPublicId("new-uuid");
            book.setType(EAuxiliaryBookType.INVENTORY_AND_BALANCES);
            book.setCreatedAt(newTime);

            // Assert
            Assertions.assertThat(book.getId()).isEqualTo(3L);
            Assertions.assertThat(book.getPublicId()).isEqualTo("new-uuid");
            Assertions.assertThat(book.getType()).isEqualTo(EAuxiliaryBookType.INVENTORY_AND_BALANCES);
            Assertions.assertThat(book.getCreatedAt()).isEqualTo(newTime);
        }

        @Test
        @DisplayName("Debería cambiar el formato con el método changeFormat")
        void testChangeFormat() {
            // Arrange
            // 'auxiliaryBook' se crea con PDF en setUp()
            Assertions.assertThat(auxiliaryBook.getFormat()).isEqualTo(EAuxiliaryBookFormat.PDF);

            // Act
            auxiliaryBook.changeFormat(EAuxiliaryBookFormat.EXCEL);

            // Assert
            Assertions.assertThat(auxiliaryBook.getFormat()).isEqualTo(EAuxiliaryBookFormat.EXCEL);
        }

        @Test
        @DisplayName("Debería manejar correctamente Equals y HashCode (provistos por @Data)")
        void testEqualsAndHashCode() {
            // Arrange: Crear una copia exacta
            AuxiliaryBook bookCopy = AuxiliaryBook.builder()
                    .id(1L)
                    .publicId("uuid-test-123")
                    .type(EAuxiliaryBookType.INVENTORY_AND_BALANCES)
                    .entId("ent-001")
                    .userId(100L)
                    .format(EAuxiliaryBookFormat.PDF)
                    .template(mockTemplate)
                    .criteria(mockCriteria)
                    .createdAt(testTime)
                    .build();

            // Arrange: Crear un objeto diferente
            AuxiliaryBook differentBook = AuxiliaryBook.builder()
                    .id(2L) // ID diferente
                    .publicId("uuid-test-999")
                    .build();

            // Assert
            Assertions.assertThat(auxiliaryBook).isEqualTo(bookCopy)
                    .isNotEqualTo(differentBook)
                    .isNotEqualTo(null)
                    .isNotEqualTo(new Object());

            Assertions.assertThat(auxiliaryBook.hashCode()).isEqualTo(bookCopy.hashCode())
                    .isNotEqualTo(differentBook.hashCode());
        }

        @Test
        @DisplayName("Debería generar un String no nulo (provisto por @Data)")
        void testToString() {
            // Act
            String bookString = auxiliaryBook.toString();

            // Assert
            Assertions.assertThat(bookString).isNotNull()
                    .contains("AuxiliaryBook")
                    .contains("id=1")
                    .contains("publicId=uuid-test-123")
                    .contains("entId=ent-001");
        }
}
