package com.tfg.egm.repository;

import com.tfg.egm.entity.Producto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findBySubcategoriaId(int id);
    boolean existsByReferencia(String referencia);
    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR LOWER(p.marca) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Producto> buscarPorNombreProducto(@Param("texto") String texto);
    @Query("SELECT p FROM Producto p WHERE (LOWER(p.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR LOWER(p.marca) LIKE LOWER(CONCAT('%', :texto, '%'))) AND p.subcategoria.id = :id")
    List<Producto> buscarPorNombreProductoIdSubcategoria(@Param("texto") String texto, @Param("id") Long id);
    @Query(value = """
        SELECT p FROM Producto p
        WHERE CAST(p.id AS string) LIKE %:texto%
        OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
        OR LOWER(p.marca) LIKE LOWER(CONCAT('%', :texto, '%'))
        OR LOWER(p.referencia) LIKE LOWER(CONCAT('%', :texto, '%'))
        OR CAST(p.tipoIVA AS string) LIKE %:texto%
        OR CAST(p.cantidad AS string) LIKE %:texto%
        OR CAST(p.stock AS string) LIKE %:texto%
        OR CAST(p.precio AS string) LIKE %:texto%
        OR LOWER(p.categoria.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
        OR LOWER(p.subcategoria.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
    """)
    List<Producto> buscarProductoAdmin(@Param("texto") String texto);
}
