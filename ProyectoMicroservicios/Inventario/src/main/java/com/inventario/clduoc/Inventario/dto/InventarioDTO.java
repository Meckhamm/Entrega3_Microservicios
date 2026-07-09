package com.inventario.clduoc.Inventario.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class InventarioDTO {

    @NotNull(message = "El productoId es obligatorio")
    @Positive(message = "El productoId debe ser mayor a cero")
    private Long productoId;

    @NotNull(message = "El stockActual es obligatorio")
    @Min(value = 0, message = "El stockActual no puede ser negativo")
    private Integer stockActual;

    @NotNull(message = "El stockMinimo es obligatorio")
    @Min(value = 0, message = "El stockMinimo no puede ser negativo")
    private Integer stockMinimo;

    @NotBlank(message = "La ubicacion es obligatoria")
    @Size(max = 120, message = "La ubicacion no puede superar los 120 caracteres")
    private String ubicacion;
}
