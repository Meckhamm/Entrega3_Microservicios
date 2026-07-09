package com.inventario.clduoc.Inventario.service;

import com.inventario.clduoc.Inventario.client.ProductoClient;
import com.inventario.clduoc.Inventario.dto.InventarioDTO;
import com.inventario.clduoc.Inventario.exception.BusinessRuleException;
import com.inventario.clduoc.Inventario.model.Inventario;
import com.inventario.clduoc.Inventario.repository.InventarioRepository;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    @Mock
    private InventarioRepository repository;

    @Mock
    private ProductoClient productoClient;

    @InjectMocks
    private InventarioService service;

    @Test
    void crearGuardaInventarioCuandoProductoExiste() {
        InventarioDTO dto = dtoValido();
        Inventario guardado = inventario(1L, dto.getProductoId());
        when(productoClient.obtenerProducto(dto.getProductoId())).thenReturn(Map.of("id", dto.getProductoId()));
        when(repository.findByProductoId(dto.getProductoId())).thenReturn(Optional.empty());
        when(repository.save(any(Inventario.class))).thenReturn(guardado);

        Inventario resultado = service.crear(dto);

        assertEquals(1L, resultado.getId());
        assertEquals(dto.getProductoId(), resultado.getProductoId());
        verify(repository).save(any(Inventario.class));
    }

    @Test
    void crearRechazaInventarioDuplicadoParaProducto() {
        InventarioDTO dto = dtoValido();
        when(productoClient.obtenerProducto(dto.getProductoId())).thenReturn(Map.of("id", dto.getProductoId()));
        when(repository.findByProductoId(dto.getProductoId())).thenReturn(Optional.of(inventario(3L, dto.getProductoId())));

        assertThrows(BusinessRuleException.class, () -> service.crear(dto));
        verify(repository, never()).save(any(Inventario.class));
    }

    @Test
    void crearRechazaProductoRemotoInexistente() {
        InventarioDTO dto = dtoValido();
        when(productoClient.obtenerProducto(dto.getProductoId())).thenThrow(feignNotFound());

        assertThrows(BusinessRuleException.class, () -> service.crear(dto));
        verify(repository, never()).save(any(Inventario.class));
    }

    private InventarioDTO dtoValido() {
        InventarioDTO dto = new InventarioDTO();
        dto.setProductoId(10L);
        dto.setStockActual(12);
        dto.setStockMinimo(5);
        dto.setUbicacion("Bodega A");
        return dto;
    }

    private Inventario inventario(Long id, Long productoId) {
        return Inventario.builder()
                .id(id)
                .productoId(productoId)
                .stockActual(12)
                .stockMinimo(5)
                .ubicacion("Bodega A")
                .build();
    }

    private FeignException feignNotFound() {
        Request request = Request.create(
                Request.HttpMethod.GET,
                "/productos/10",
                Map.of(),
                null,
                StandardCharsets.UTF_8,
                null
        );
        return new FeignException.NotFound("Producto no encontrado", request, null, Map.of());
    }
}
