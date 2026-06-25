package cl.duocuc.Producto;

import cl.duocuc.Producto.model.Producto;
import cl.duocuc.Producto.repository.ProductoRepository;
import cl.duocuc.Producto.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository repository;

    @InjectMocks
    private ProductoService service;

    @Test
    void listarDebeRetornarProductos() {

        List<Producto> productos = Arrays.asList(
                new Producto(),
                new Producto()
        );

        when(repository.findAll()).thenReturn(productos);

        List<Producto> resultado = service.listar();

        assertEquals(2, resultado.size());
        verify(repository).findAll();
    }

    @Test
    void buscarPorIdDebeRetornarProducto() {

        Producto producto = new Producto();

        when(repository.findById(1L))
                .thenReturn(Optional.of(producto));

        Optional<Producto> resultado = service.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        verify(repository).findById(1L);
    }

    @Test
    void guardarDebePersistirProducto() {

        Producto producto = new Producto();

        when(repository.save(producto))
                .thenReturn(producto);

        Producto resultado = service.guardar(producto);

        assertNotNull(resultado);
        verify(repository).save(producto);
    }

    @Test
    void eliminarDebeInvocarRepository() {

        service.eliminar(1L);

        verify(repository).deleteById(1L);
    }
}