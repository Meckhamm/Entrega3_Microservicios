package cl.duocuc.Producto.repository;

import cl.duocuc.Producto.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductoRepository extends JpaRepository<Producto, Long> {
}