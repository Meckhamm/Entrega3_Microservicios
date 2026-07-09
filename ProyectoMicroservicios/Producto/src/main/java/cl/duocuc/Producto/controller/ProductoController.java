package cl.duocuc.Producto.controller;

import cl.duocuc.Producto.dto.ProductoDTO;
import cl.duocuc.Producto.model.Producto;
import cl.duocuc.Producto.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Lista todos los productos")
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca un producto por ID")
    public ResponseEntity<Producto> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crea un producto validando datos de entrada")
    public ResponseEntity<Map<String, Object>> guardar(@Valid @RequestBody ProductoDTO producto) {
        Producto nuevoProducto = service.guardar(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "codigo", 201,
                "mensaje", "Producto creado exitosamente",
                "producto", nuevoProducto,
                "fecha", LocalDateTime.now()
        ));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un producto existente")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Long id,
                                                          @Valid @RequestBody ProductoDTO producto) {
        Producto productoActualizado = service.actualizar(id, producto);
        return ResponseEntity.ok(Map.of(
                "codigo", 200,
                "mensaje", "Producto actualizado exitosamente",
                "producto", productoActualizado,
                "fecha", LocalDateTime.now()
        ));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un producto existente")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok(Map.of(
                "codigo", 200,
                "mensaje", "Producto eliminado exitosamente",
                "fecha", LocalDateTime.now()
        ));
    }
}
