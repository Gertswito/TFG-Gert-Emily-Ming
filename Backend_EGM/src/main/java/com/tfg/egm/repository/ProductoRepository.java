package com.tfg.egm.repository;

import com.tfg.egm.entity.Producto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositorio JPA para la entidad Producto.
 * Proporciona métodos para consultar, buscar y comprobar la existencia de productos.
 */
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Busca todos los productos de una subcategoría por su ID.
     * @param id identificador de la subcategoría
     * @return lista de productos de la subcategoría
     */
    List<Producto> findBySubcategoriaId(int id);

    /**
     * Comprueba si existe un producto por su referencia.
     * @param referencia referencia interna del producto
     * @return true si existe, false si no
     */
    boolean existsByReferencia(String referencia);

    /**
     * Busca todos los productos cuyo stock sea menor a 100.
     * @return lista de productos con stock bajo
     */
    @Query("SELECT p FROM Producto p WHERE p.stock < 100")
    List<Producto> findByStockBajo();

    /**
     * Busca productos por nombre o marca que contengan el texto dado (ignorando mayúsculas/minúsculas).
     * @param texto texto de búsqueda
     * @return lista de productos encontrados
     */
    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR LOWER(p.marca) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Producto> buscarPorNombreProducto(@Param("texto") String texto);

    /**
     * Busca productos por nombre o marca y por subcategoría.
     * @param texto texto de búsqueda
     * @param id identificador de la subcategoría
     * @return lista de productos encontrados
     */
    @Query("SELECT p FROM Producto p WHERE (LOWER(p.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR LOWER(p.marca) LIKE LOWER(CONCAT('%', :texto, '%'))) AND p.subcategoria.id = :id")
    List<Producto> buscarPorNombreProductoIdSubcategoria(@Param("texto") String texto, @Param("id") Long id);

    /**
     * Busca productos para administración filtrando por varios campos.
     * @param texto texto de búsqueda
     * @return lista de productos encontrados
     */
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
