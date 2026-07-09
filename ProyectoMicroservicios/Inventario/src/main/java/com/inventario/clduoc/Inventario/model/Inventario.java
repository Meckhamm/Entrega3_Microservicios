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

    @Column(nullable = false, unique = true)
    private Long productoId;

    @PositiveOrZero
    @Column(nullable = false)
    private Integer stockActual;

    @PositiveOrZero
    @Column(nullable = false)
    private Integer stockMinimo;

    @Column(nullable = false, length = 120)
    private String ubicacion;
}
