package com.inventario.clduoc.Inventario.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Entity
@Table(name = "inventario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productoId;

    @Schema(
            description = "Cantidad actual disponible del producto en inventario",
            example = "25"
    )
    @PositiveOrZero
    private Integer stockActual;

    @Schema(
            description = "Cantidad mínima permitida antes de considerar el producto con stock bajo",
            example = "5"
    )
    @PositiveOrZero
    private Integer stockMinimo;

    private String ubicacion;
}