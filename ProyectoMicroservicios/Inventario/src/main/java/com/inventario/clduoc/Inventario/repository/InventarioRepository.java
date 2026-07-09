package com.inventario.clduoc.Inventario.repository;

import com.inventario.clduoc.Inventario.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    Optional<Inventario> findByProductoId(Long productoId);

    @Query("select i from Inventario i where i.stockActual <= i.stockMinimo")
    List<Inventario> findConStockBajo();
}
