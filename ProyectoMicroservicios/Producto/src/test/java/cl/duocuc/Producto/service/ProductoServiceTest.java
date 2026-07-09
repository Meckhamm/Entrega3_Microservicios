package cl.duocuc.Producto.service;

import cl.duocuc.Producto.dto.ProductoDTO;
import cl.duocuc.Producto.exception.BusinessRuleException;
import cl.duocuc.Producto.exception.ResourceNotFoundException;
import cl.duocuc.Producto.model.Producto;
import cl.duocuc.Producto.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository repository;

    @InjectMocks
    private ProductoService service;

    @Test
    void guardarCreaProductoCuandoDatosSonValidos() {
        ProductoDTO dto = dtoValido();
        Producto guardado = producto(1L);
        when(repository.save(any(Producto.class))).thenReturn(guardado);

        Producto resultado = service.guardar(dto);

        assertEquals(1L, resultado.getId());
        assertEquals("Notebook", resultado.getNombre());
        verify(repository).save(any(Producto.class));
    }

    @Test
    void guardarRechazaPrecioMenorAlMinimo() {
        ProductoDTO dto = dtoValido();
        dto.setPrecio(50.0);

        assertThrows(BusinessRuleException.class, () -> service.guardar(dto));
        verify(repository, never()).save(any(Producto.class));
    }

    @Test
    void actualizarLanzaNotFoundCuandoProductoNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.actualizar(99L, dtoValido()));
    }

    @Test
    void listarRetornaListaVaciaCuandoNoHayProductos() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<Producto> resultado = service.listar();

        assertTrue(resultado.isEmpty());
        verify(repository).findAll();
    }

    private ProductoDTO dtoValido() {
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("Notebook");
        dto.setDescripcion("Equipo para ventas");
        dto.setPrecio(450000.0);
        dto.setStock(8);
        dto.setCategoria("Computacion");
        dto.setProveedor("Duoc Store");
        return dto;
    }

    private Producto producto(Long id) {
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre("Notebook");
        producto.setDescripcion("Equipo para ventas");
        producto.setPrecio(450000.0);
        producto.setStock(8);
        producto.setCategoria("Computacion");
        producto.setProveedor("Duoc Store");
        return producto;
    }
}
