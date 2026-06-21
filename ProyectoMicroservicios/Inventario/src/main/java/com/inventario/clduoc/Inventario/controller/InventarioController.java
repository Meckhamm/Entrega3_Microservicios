package com.inventario.clduoc.Inventario.controller;

import com.inventario.clduoc.Inventario.dto.InventarioDTO;
import com.inventario.clduoc.Inventario.model.Inventario;
import com.inventario.clduoc.Inventario.service.InventarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService service;

    @GetMapping
    public List<Inventario> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Inventario obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(
            @Valid @RequestBody InventarioDTO dto) {

        Inventario inventario = service.crear(dto);

        Map<String, Object> response = new HashMap<>();
        response.put("codigo", 201);
        response.put("mensaje", "Inventario creado exitosamente");
        response.put("data", inventario);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody InventarioDTO dto) {

        Inventario inventarioActualizado = service.actualizar(id, dto);

        Map<String, Object> response = new HashMap<>();
        response.put("codigo", 200);
        response.put("mensaje", "Inventario actualizado exitosamente");
        response.put("data", inventarioActualizado);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id) {

        service.eliminar(id);

        Map<String, Object> response = new HashMap<>();
        response.put("codigo", 200);
        response.put("mensaje", "Inventario eliminado exitosamente");

        return ResponseEntity.ok(response);
    }
}