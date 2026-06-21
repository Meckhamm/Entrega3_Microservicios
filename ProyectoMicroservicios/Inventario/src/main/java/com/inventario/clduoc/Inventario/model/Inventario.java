package com.inventario.clduoc.Inventario.model;

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

    @PositiveOrZero
    private Integer stockActual;

    @PositiveOrZero
    private Integer stockMinimo;

    private String ubicacion;
}