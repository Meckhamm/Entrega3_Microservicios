package com.inventario.clduoc.Inventario.controller;

import com.inventario.clduoc.Inventario.dto.InventarioDTO;
import com.inventario.clduoc.Inventario.model.Inventario;
import com.inventario.clduoc.Inventario.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService service;

    @GetMapping
    @Operation(summary = "Lista todos los registros de inventario")
    public ResponseEntity<List<Inventario>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un inventario por ID")
    public ResponseEntity<Inventario> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/producto/{productoId}")
    @Operation(summary = "Obtiene el inventario asociado a un producto")
    public ResponseEntity<Inventario> obtenerPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(service.obtenerPorProductoId(productoId));
    }

    @GetMapping("/stock-bajo")
    @Operation(summary = "Lista inventarios cuyo stock actual es menor o igual al stock minimo")
    public ResponseEntity<List<Inventario>> listarStockBajo() {
        return ResponseEntity.ok(service.listarStockBajo());
    }

    @PostMapping
    @Operation(summary = "Crea inventario validando que el producto exista")
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody InventarioDTO dto) {
        Inventario inventario = service.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "codigo", 201,
                "mensaje", "Inventario creado exitosamente",
                "data", inventario,
                "fecha", LocalDateTime.now()
        ));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un registro de inventario")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Long id,
                                                          @Valid @RequestBody InventarioDTO dto) {
        Inventario inventarioActualizado = service.actualizar(id, dto);
        return ResponseEntity.ok(Map.of(
                "codigo", 200,
                "mensaje", "Inventario actualizado exitosamente",
                "data", inventarioActualizado,
                "fecha", LocalDateTime.now()
        ));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un registro de inventario")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok(Map.of(
                "codigo", 200,
                "mensaje", "Inventario eliminado exitosamente",
                "fecha", LocalDateTime.now()
        ));
    }
}
