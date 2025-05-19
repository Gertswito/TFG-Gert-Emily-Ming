package com.tfg.egm.repository;

import com.tfg.egm.entity.Subcategoria;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositorio JPA para la entidad Subcategoria.
 * Proporciona métodos para consultar, buscar y comprobar la existencia de subcategorías.
 */
public interface SubcategoriaRepository extends JpaRepository<Subcategoria, Long> {

    /**
     * Busca todas las subcategorías de una categoría por su ID.
     * @param id identificador de la categoría
     * @return lista de subcategorías de la categoría
     */
    List<Subcategoria> findByCategoriaId(int id);

    /**
     * Comprueba si existe una subcategoría por su nombre.
     * @param nombre nombre de la subcategoría
     * @return true si existe, false si no
     */
    boolean existsByNombre(String nombre);

    /**
     * Busca una subcategoría por su nombre.
     * @param nombre nombre de la subcategoría
     * @return la subcategoría encontrada o null
     */
    Subcategoria findByNombre(String nombre);

    /**
     * Busca subcategorías cuyo nombre contenga el texto dado (ignorando mayúsculas/minúsculas).
     * @param texto texto de búsqueda
     * @return lista de subcategorías encontradas
     */
    @Query("SELECT s FROM Subcategoria s WHERE LOWER(s.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Subcategoria> subcategoriaBusquedaNombres(@Param("texto") String texto);

    /**
     * Busca subcategorías por nombre y por categoría.
     * @param texto texto de búsqueda
     * @param id identificador de la categoría
     * @return lista de subcategorías encontradas
     */
    @Query("SELECT s FROM Subcategoria s WHERE LOWER(s.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) AND s.categoria.id = :id")
    List<Subcategoria> subcategoriaBusquedaNombres(@Param("texto") String texto, @Param("id") Long id);

    /**
     * Busca subcategorías para administración filtrando por nombre, nombre de la categoría o ID.
     * @param texto texto de búsqueda
     * @return lista de subcategorías encontradas
     */
    @Query("""
        SELECT s FROM Subcategoria s
        WHERE LOWER(s.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
        OR LOWER(s.categoria.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
        OR CAST(s.id AS string) LIKE %:texto%
    """)
    List<Subcategoria> buscarSubcategoriaAdmin(@Param("texto") String texto);
}