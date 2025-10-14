package com.unicauca.edu.co.auxiliary_book.application.useCase.export.report.builders;

import com.unicauca.edu.co.auxiliary_book.domain.models.core.criteria.AuxiliaryBookCriteria;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.export.AuxiliaryBookTemplate;
import com.unicauca.edu.co.auxiliary_book.domain.models.core.export.ExportInfo;
import com.unicauca.edu.co.auxiliary_book.domain.models.enums.EAlignment; // Asegúrate de importar tu enum
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.component.*;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@NoArgsConstructor
public class ReportStyleBuilder {

    private StyleBuilder textStyle;
    private StyleBuilder headerStyle;
    private StyleBuilder titleStyle;
    private StyleBuilder criteriaTitleStyle;
    private StyleBuilder tableCellStyle;

    private void setTextStyle(AuxiliaryBookTemplate template) {
        // ESTILO BASE - SIMPLE Y LIMPIO
        this.textStyle = DynamicReports.stl.style()
                .setFontName(template.getFont())
                .setFontSize(template.getFontSize())
                .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE);
    }

    private void setHeaderStyle(AuxiliaryBookTemplate template) {
        this.headerStyle = DynamicReports.stl.style() // Ya no hereda de textStyle para los bordes
                .setFontName(template.getFont())
                .setFontSize(16)
                .setBold(true)
                .setBackgroundColor(java.awt.Color.decode(template.getMainColor()))
                .setForegroundColor(java.awt.Color.WHITE)
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE)
                .setBorder(DynamicReports.stl.pen().setLineWidth(0.5f).setLineColor(java.awt.Color.BLACK));
    }

    private void setTitleAndCriteriaStyle(AuxiliaryBookTemplate template) {
        this.titleStyle = DynamicReports.stl.style(this.textStyle)
                .setFontSize(template.getFontSize() + 6) // Un poco más grande
                .setBold(true);

        this.criteriaTitleStyle = DynamicReports.stl.style(this.textStyle)
                .setBold(true)
                .setTopPadding(10); // Espacio antes de los criterios
    }

    private void setTableCellStyle(AuxiliaryBookTemplate template) {
        this.tableCellStyle = DynamicReports.stl.style()
                .setFontName(template.getFont())
                .setFontSize(template.getFontSize())
                .setBorder(DynamicReports.stl.pen().setLineWidth(0.5f).setLineColor(java.awt.Color.BLACK))
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE)
                .setPadding(5);
    }

    private ImageBuilder buildLogoImage(URL logoUrl) {
        System.out.println("Intentando cargar logo desde URL: " + logoUrl);
        if (logoUrl == null) {
            return Components.image(new ByteArrayInputStream(new byte[0]))
                    .setFixedDimension(1, 1);
        }

        try {
            // 1. Abrimos una conexión HTTP en lugar de un stream simple.
            HttpsURLConnection connection = (HttpsURLConnection) logoUrl.openConnection();

            // 2. ¡EL PASO CLAVE! Nos identificamos como un navegador Firefox.
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:99.0) Gecko/20100101 Firefox/99.0");

            // 3. Obtenemos el stream desde esta conexión configurada.
            InputStream imageStream = connection.getInputStream();

            // 4. Le pasamos el stream ya validado a DynamicReports.
            return Components.image(imageStream)
                    .setFixedHeight(50)
                    .setFixedWidth(120);

        } catch (Exception e) {
            System.err.println("DynamicReports no pudo cargar la imagen desde la URL: " + logoUrl + ". Error: " + e.getMessage());
            return Components.image(new ByteArrayInputStream(new byte[0]))
                    .setFixedDimension(1, 1);
        }
    }

    private VerticalListBuilder buildCriteriaComponent(AuxiliaryBookCriteria criteria, StyleBuilder baseStyle) {
        // La recolección de criterios no cambia...
        StyleBuilder labelStyle = DynamicReports.stl.style(baseStyle).setBold(true);
        StyleBuilder valueStyle = DynamicReports.stl.style(baseStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);
        List<ComponentBuilder<?, ?>> criteriaItems = new ArrayList<>();

        // ... (toda la lógica 'if' para añadir criterios a la lista no cambia)
        if (criteria.getCriteriaType() != null) {
            criteriaItems.add(createCriteriaRow("Tipo de Nivel:", criteria.getCriteriaType().getDisplayName(), labelStyle, valueStyle));
        }
        // ... etc ...
        if (criteria.hasRange()) {
            String range = criteria.getCriteriaRange().getFromRange() + " a " + criteria.getCriteriaRange().getToRange();
            criteriaItems.add(createCriteriaRow("Rango:", range, labelStyle, valueStyle));
        }
        if (criteria.getCostCenterId() != null) {
            criteriaItems.add(createCriteriaRow("Centro de costo:", criteria.getCostCenterId(), labelStyle, valueStyle));
        }
        if (criteria.getThirdPartyId() != null) {
            criteriaItems.add(createCriteriaRow("Tercero:", criteria.getThirdPartyId(), labelStyle, valueStyle));
        }
        if (criteria.getVoucherType() != null) {
            criteriaItems.add(createCriteriaRow("Tipo de comprobante:", criteria.getVoucherType(), labelStyle, valueStyle));
        }
        if (criteria.getStartDate() != null) {
            criteriaItems.add(createCriteriaRow("Fecha de Inicio:", criteria.getStartDate().toString(), labelStyle, valueStyle));
        }
        if (criteria.getEndDate() != null) {
            criteriaItems.add(createCriteriaRow("Fecha de Corte:", criteria.getEndDate().toString(), labelStyle, valueStyle));
        }


        // --- CÓDIGO RESTAURADO Y CORRECTO ---
        // Volvemos a añadir el verticalGap en el bucle.
        VerticalListBuilder leftColumn = Components.verticalList();
        VerticalListBuilder rightColumn = Components.verticalList();
        int middle = (int) Math.ceil(criteriaItems.size() / 2.0);

        for (int i = 0; i < criteriaItems.size(); i++) {
            ComponentBuilder<?, ?> item = criteriaItems.get(i);

            if (i < middle) {
                leftColumn.add(item);
                leftColumn.add(Components.verticalGap(8)); // Añadimos el espacio aquí
            } else {
                rightColumn.add(item);
                rightColumn.add(Components.verticalGap(8)); // Y aquí
            }
        }

        // El ensamblado final no cambia
        HorizontalListBuilder columns = Components.horizontalList().add(leftColumn, rightColumn).setGap(40);
        VerticalListBuilder finalComponent = Components.verticalList();
        finalComponent.add(Components.text("Criterios Utilizados:").setStyle(criteriaTitleStyle));
        if (!criteriaItems.isEmpty()) {
            finalComponent.add(columns);
        }
        return finalComponent;
    }

    private VerticalListBuilder createCriteriaRow(String label, String value, StyleBuilder labelStyle, StyleBuilder valueStyle) {
        // Simplemente apilamos la etiqueta y el valor.
        return Components.verticalList(
                Components.text(label).setStyle(labelStyle),
                Components.text(value).setStyle(valueStyle)
        );
    }


    private void setTitle(JasperReportBuilder report, AuxiliaryBookTemplate template, ExportInfo exportInfo) {
        EAlignment alignment = template.getAlienation() != null ? template.getAlienation() : EAlignment.RIGHT;

        // Determinar la alineación del texto para Títulos y Fecha
        HorizontalTextAlignment textAlignment = switch (alignment) {
            case LEFT -> HorizontalTextAlignment.LEFT;
            case RIGHT -> HorizontalTextAlignment.RIGHT;
            default -> HorizontalTextAlignment.CENTER;
        };

        // --- AJUSTE EN LA ALINEACIÓN DEL TEXTO DE LA FECHA ---
        ComponentBuilder<?, ?> dateTimeComponent = Components.verticalList(
                Components.text("Generado en:")
                        .setStyle(DynamicReports.stl.style(textStyle).setBold(true))
                        .setHorizontalTextAlignment(textAlignment), // Alineación dinámica

                Components.currentDate()
                        .setPattern("HH:mm dd/MM/yyyy")
                        .setStyle(textStyle)
                        .setHorizontalTextAlignment(textAlignment) // Alineación dinámica
        );

        // El resto de la lógica de posicionamiento de bloques no cambia...
        ImageBuilder logo = buildLogoImage(template.getPathLogotype());
        ComponentBuilder<?, ?> titles = Components.verticalList(
                Components.text(exportInfo.getEntName()).setStyle(titleStyle).setHorizontalTextAlignment(textAlignment),
                Components.text(template.getName()).setStyle(textStyle).setHorizontalTextAlignment(textAlignment)
        );
        HorizontalListBuilder headerList = Components.horizontalList().setGap(10);
        switch (alignment) {
            case LEFT:
                headerList.add(logo, titles).add(Components.filler()).add(dateTimeComponent);
                break;
            case CENTER:
                headerList.add(Components.filler(), logo, titles, Components.filler()).add(dateTimeComponent);
                break;
            case RIGHT:
            default:
                headerList.add(dateTimeComponent).add(Components.filler()).add(titles, logo);
                break;
        }

        VerticalListBuilder criteriaList = buildCriteriaComponent(exportInfo.getCriteriaUsed(), this.textStyle);
        // Agregamos una línea separadora como en la imagen
        report.title(
                Components.verticalList(
                        headerList,
                        Components.verticalGap(10), // Espacio antes de la línea
                        Components.line(),          // La línea (correcta, 1px de alto)
                        Components.verticalGap(5),  // Espacio (menor) después de la línea
                        criteriaList,
                        Components.verticalGap(20)
                )
        );
    }

    private void setPageFooter(JasperReportBuilder report, AuxiliaryBookTemplate template) {
        EAlignment alignment = template.getAlienation() != null ? template.getAlienation() : EAlignment.RIGHT;
        StyleBuilder footerStyle = DynamicReports.stl.style(textStyle).setFontSize(template.getFontSize() - 2);

        // ✅ CORRECCIÓN DE PAGINACIÓN AQUÍ
        HorizontalListBuilder pageNumberComponent = Components.horizontalList(
                Components.pageNumber().setStyle(footerStyle),
                Components.text(" de ").setStyle(footerStyle),
                Components.totalPages().setStyle(footerStyle)
        );

        HorizontalListBuilder footer = Components.horizontalList();

        // ... (El switch de alineación del footer no cambia)
        switch (alignment) {
            case LEFT:
                footer.add(pageNumberComponent, Components.filler());
                break;
            case CENTER:
                footer.add(Components.filler(), pageNumberComponent, Components.filler());
                break;
            case RIGHT:
            default:
                footer.add(Components.filler(), pageNumberComponent);
                break;
        }
        report.pageFooter(footer);
    }

    public void templateBuilder(JasperReportBuilder report, AuxiliaryBookTemplate template, ExportInfo exportInfo) {
        // 1. Definir todos los estilos
        this.setTextStyle(template);       // Estilo base
        this.setTableCellStyle(template);  // NUEVO: Estilo para la tabla
        this.setHeaderStyle(template);
        this.setTitleAndCriteriaStyle(template);

        // 2. Construir las secciones del reporte
        this.setTitle(report, template, exportInfo);
        this.setPageFooter(report, template);

        // 3. Aplicar los estilos de la tabla al template
        report.setTemplate(
                DynamicReports.template()
                        // Usamos los estilos específicos de la tabla aquí
                        .setColumnStyle(this.tableCellStyle)
                        .setColumnTitleStyle(this.headerStyle)
        );
    }
}