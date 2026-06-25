package cl.duocuc.Producto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modelo que representa un producto del sistema")
public class Producto {

    @Schema(description = "Identificador único del producto", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nombre del producto", example = "Mouse Gamer")
    @NotBlank
    private String nombre;

    @Schema(description = "Descripción del producto", example = "Mouse óptico con luces RGB")
    private String descripcion;

    @Schema(description = "Precio del producto", example = "15990")
    @Positive
    private Double precio;

    @Schema(description = "Stock disponible del producto", example = "20")
    @PositiveOrZero
    private Integer stock;

    @Schema(description = "Categoría del producto", example = "Tecnología")
    private String categoria;

    @Schema(description = "Proveedor del producto", example = "Logitech")
    private String proveedor;
}