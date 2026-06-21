package cl.duocuc.Producto.controller;

import cl.duocuc.Producto.model.Producto;
import cl.duocuc.Producto.service.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
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
    public List<Producto> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {

        return service.buscarPorId(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> {

                    Map<String, Object> error = new HashMap<>();

                    error.put("codigo", 404);
                    error.put("mensaje", "Producto no encontrado");
                    error.put("fecha", LocalDateTime.now());

                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> guardar(@RequestBody Producto producto) {

        Producto nuevoProducto = service.guardar(producto);

        Map<String, Object> respuesta = new HashMap<>();

        respuesta.put("codigo", 201);
        respuesta.put("mensaje", "Producto creado exitosamente");
        respuesta.put("producto", nuevoProducto);
        respuesta.put("fecha", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Long id,
                                                          @RequestBody Producto producto) {

        Producto productoActualizado = service.actualizar(id, producto);

        Map<String, Object> respuesta = new HashMap<>();

        respuesta.put("codigo", 200);
        respuesta.put("mensaje", "Producto actualizado exitosamente");
        respuesta.put("producto", productoActualizado);
        respuesta.put("fecha", LocalDateTime.now());

        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id) {

        service.eliminar(id);

        Map<String, Object> respuesta = new HashMap<>();

        respuesta.put("codigo", 200);
        respuesta.put("mensaje", "Producto eliminado exitosamente");
        respuesta.put("fecha", LocalDateTime.now());

        return ResponseEntity.ok(respuesta);
    }
}