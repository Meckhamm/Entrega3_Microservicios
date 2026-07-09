package cl.duocuc.Producto.service;

import cl.duocuc.Producto.dto.ProductoDTO;
import cl.duocuc.Producto.exception.BusinessRuleException;
import cl.duocuc.Producto.exception.ResourceNotFoundException;
import cl.duocuc.Producto.model.Producto;
import cl.duocuc.Producto.repository.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private static final Logger log = LoggerFactory.getLogger(ProductoService.class);

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<Producto> listar() {
        return repository.findAll();
    }

    public Producto buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id " + id));
    }

    public Producto guardar(ProductoDTO dto) {
        validarReglas(dto);
        Producto producto = toEntity(dto, new Producto());
        log.info("Creando producto nombre={} categoria={}", dto.getNombre(), dto.getCategoria());
        return repository.save(producto);
    }

    public Producto actualizar(Long id, ProductoDTO dto) {
        validarReglas(dto);
        Producto producto = buscarPorId(id);
        toEntity(dto, producto);
        log.info("Actualizando producto id={} nombre={}", id, dto.getNombre());
        return repository.save(producto);
    }

    public void eliminar(Long id) {
        Producto producto = buscarPorId(id);
        log.info("Eliminando producto id={} nombre={}", id, producto.getNombre());
        repository.delete(producto);
    }

    private void validarReglas(ProductoDTO dto) {
        if (dto.getPrecio() != null && dto.getPrecio() < 100) {
            throw new BusinessRuleException("El precio minimo de un producto es 100");
        }
    }

    private Producto toEntity(ProductoDTO dto, Producto producto) {
        producto.setNombre(dto.getNombre().trim());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setCategoria(dto.getCategoria().trim());
        producto.setProveedor(dto.getProveedor().trim());
        return producto;
    }
}
