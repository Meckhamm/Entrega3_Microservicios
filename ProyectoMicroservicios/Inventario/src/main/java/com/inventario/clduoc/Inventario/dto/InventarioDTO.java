package com.inventario.clduoc.Inventario.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InventarioDTO {

    @NotNull(message = "El productoId es obligatorio")
    private Long productoId;

    @NotNull(message = "El stockActual es obligatorio")
    @Min(value = 0, message = "El stockActual no puede ser negativo")
    private Integer stockActual;

    @NotNull(message = "El stockMinimo es obligatorio")
    @Min(value = 0, message = "El stockMinimo no puede ser negativo")
    private Integer stockMinimo;

    private String ubicacion;
}