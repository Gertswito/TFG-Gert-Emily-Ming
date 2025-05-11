package com.tfg.egm.repository;

import com.tfg.egm.entity.Categoria;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    boolean existsByNombre(String nombre);
    Categoria findByNombre(String nombre);
    @Query("""
        SELECT DISTINCT c FROM Categoria c
        LEFT JOIN c.subcategorias s
        WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
        OR LOWER(s.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
    """)
    List<Categoria> categoriaBusquedaNombres(@Param("texto") String texto);
    @Query("SELECT c FROM Categoria c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR CAST(c.id AS string) LIKE %:texto%")
    List<Categoria> buscarCategoriaAdmin(@Param("texto") String texto);
}