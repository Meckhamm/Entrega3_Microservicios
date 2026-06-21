package com.inventario.clduoc.Inventario.service;

import com.inventario.clduoc.Inventario.client.ProductoClient;
import com.inventario.clduoc.Inventario.dto.InventarioDTO;
import com.inventario.clduoc.Inventario.exception.ResourceNotFoundException;
import com.inventario.clduoc.Inventario.model.Inventario;
import com.inventario.clduoc.Inventario.repository.InventarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventarioService {

    private final InventarioRepository repository;
    private final ProductoClient productoClient;

    public List<Inventario> listar() {
        return repository.findAll();
    }

    public Inventario obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado"));
    }

    public Inventario crear(InventarioDTO dto) {

        if (productoClient.obtenerProducto(dto.getProductoId()) == null) {
            throw new RuntimeException("El producto no existe");
        }

        Inventario inventario = Inventario.builder()
                .productoId(dto.getProductoId())
                .stockActual(dto.getStockActual())
                .stockMinimo(dto.getStockMinimo())
                .ubicacion(dto.getUbicacion())
                .build();

        return repository.save(inventario);
    }

    public Inventario actualizar(Long id, InventarioDTO dto) {

        Inventario inv = obtenerPorId(id);

        inv.setStockActual(dto.getStockActual());
        inv.setStockMinimo(dto.getStockMinimo());
        inv.setUbicacion(dto.getUbicacion());

        return repository.save(inv);
    }

    public void eliminar(Long id) {

        Inventario inv = obtenerPorId(id);

        // Elimina el producto relacionado
        productoClient.eliminarProducto(inv.getProductoId());

        // Elimina el inventario
        repository.delete(inv);
    }
}