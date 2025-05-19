package com.tfg.egm.repository;

import com.tfg.egm.entity.Categoria;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositorio JPA para la entidad Categoria.
 * Proporciona métodos para consultar, buscar y comprobar la existencia de categorías.
 */
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    /**
     * Comprueba si existe una categoría por su nombre.
     * @param nombre nombre de la categoría
     * @return true si existe, false si no
     */
    boolean existsByNombre(String nombre);

    /**
     * Busca una categoría por su nombre.
     * @param nombre nombre de la categoría
     * @return la categoría encontrada o null
     */
    Categoria findByNombre(String nombre);

    /**
     * Busca categorías cuyo nombre o el de alguna de sus subcategorías contenga el texto dado (ignorando mayúsculas/minúsculas).
     * @param texto texto de búsqueda
     * @return lista de categorías encontradas
     */
    @Query("""
        SELECT DISTINCT c FROM Categoria c
        LEFT JOIN c.subcategorias s
        WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
        OR LOWER(s.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
    """)
    List<Categoria> categoriaBusquedaNombres(@Param("texto") String texto);

    /**
     * Busca categorías por nombre o por ID, para administración.
     * @param texto texto de búsqueda
     * @return lista de categorías encontradas
     */
    @Query("SELECT c FROM Categoria c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR CAST(c.id AS string) LIKE %:texto%")
    List<Categoria> buscarCategoriaAdmin(@Param("texto") String texto);
}