package com.inventario.clduoc.Inventario.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "productos", url = "${producto.service.url:http://localhost:8081/productos}")
public interface ProductoClient {

    @GetMapping("/{id}")
    Object obtenerProducto(@PathVariable Long id);

    @DeleteMapping("/{id}")
    void eliminarProducto(@PathVariable Long id);
}
