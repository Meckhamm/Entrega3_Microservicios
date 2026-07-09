package com.inventario.clduoc.Inventario.service;

import com.inventario.clduoc.Inventario.client.ProductoClient;
import com.inventario.clduoc.Inventario.dto.InventarioDTO;
import com.inventario.clduoc.Inventario.exception.BusinessRuleException;
import com.inventario.clduoc.Inventario.exception.RemoteServiceException;
import com.inventario.clduoc.Inventario.exception.ResourceNotFoundException;
import com.inventario.clduoc.Inventario.model.Inventario;
import com.inventario.clduoc.Inventario.repository.InventarioRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventarioService {

    private static final Logger log = LoggerFactory.getLogger(InventarioService.class);

    private final InventarioRepository repository;
    private final ProductoClient productoClient;

    public List<Inventario> listar() {
        return repository.findAll();
    }

    public Inventario obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado con id " + id));
    }

    public Inventario obtenerPorProductoId(Long productoId) {
        return repository.findByProductoId(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe inventario para el producto " + productoId));
    }

    public List<Inventario> listarStockBajo() {
        return repository.findConStockBajo();
    }

    public Inventario crear(InventarioDTO dto) {
        validarProductoRemoto(dto.getProductoId());
        repository.findByProductoId(dto.getProductoId()).ifPresent(inventario -> {
            throw new BusinessRuleException("El producto ya tiene un registro de inventario");
        });

        Inventario inventario = Inventario.builder()
                .productoId(dto.getProductoId())
                .stockActual(dto.getStockActual())
                .stockMinimo(dto.getStockMinimo())
                .ubicacion(dto.getUbicacion().trim())
                .build();

        log.info("Creando inventario productoId={} stockActual={}", dto.getProductoId(), dto.getStockActual());
        return repository.save(inventario);
    }

    public Inventario actualizar(Long id, InventarioDTO dto) {
        validarProductoRemoto(dto.getProductoId());
        Inventario inventario = obtenerPorId(id);

        repository.findByProductoId(dto.getProductoId())
                .filter(existente -> !existente.getId().equals(id))
                .ifPresent(existente -> {
                    throw new BusinessRuleException("El producto ya esta asociado a otro inventario");
                });

        inventario.setProductoId(dto.getProductoId());
        inventario.setStockActual(dto.getStockActual());
        inventario.setStockMinimo(dto.getStockMinimo());
        inventario.setUbicacion(dto.getUbicacion().trim());

        log.info("Actualizando inventario id={} productoId={}", id, dto.getProductoId());
        return repository.save(inventario);
    }

    public void eliminar(Long id) {
        Inventario inventario = obtenerPorId(id);
        log.info("Eliminando inventario id={} productoId={}", id, inventario.getProductoId());
        repository.delete(inventario);
    }

    private void validarProductoRemoto(Long productoId) {
        try {
            productoClient.obtenerProducto(productoId);
        } catch (FeignException.NotFound ex) {
            throw new BusinessRuleException("El producto no existe, primero debes crearlo");
        } catch (FeignException ex) {
            throw new RemoteServiceException("No se pudo validar el producto remoto");
        }
    }
}
