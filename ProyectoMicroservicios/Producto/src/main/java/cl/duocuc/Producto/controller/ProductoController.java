package cl.duocuc.Producto.controller;

import cl.duocuc.Producto.model.Producto;
import cl.duocuc.Producto.service.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/productos")
@Tag(name = "Productos", description = "Endpoints para administrar productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @Operation(summary = "Listar productos", description = "Obtiene todos los productos registrados")
    @ApiResponse(responseCode = "200", description = "Listado de productos obtenido correctamente")
    @GetMapping
    public List<Producto> listar() {
        return service.listar();
    }
    @Operation(summary = "Buscar producto por ID", description = "Obtiene un producto específico según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
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
    @Operation(summary = "Crear producto", description = "Registra un nuevo producto en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    })
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
    @Operation(summary = "Actualizar producto", description = "Actualiza los datos de un producto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    })
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
    @Operation(summary = "Eliminar producto", description = "Elimina un producto según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    })
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