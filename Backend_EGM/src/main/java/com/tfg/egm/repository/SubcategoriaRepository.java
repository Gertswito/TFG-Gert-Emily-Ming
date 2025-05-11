package com.tfg.egm.repository;

import com.tfg.egm.entity.Subcategoria;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubcategoriaRepository extends JpaRepository<Subcategoria, Long> {
    List<Subcategoria> findByCategoriaId(int id);
    boolean existsByNombre(String nombre);
    Subcategoria findByNombre(String nombre);
    @Query("SELECT s FROM Subcategoria s WHERE LOWER(s.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Subcategoria> subcategoriaBusquedaNombres(@Param("texto") String texto);
    @Query("SELECT s FROM Subcategoria s WHERE LOWER(s.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) AND s.categoria.id = :id")
    List<Subcategoria> subcategoriaBusquedaNombres(@Param("texto") String texto, @Param("id") Long id);
    @Query("""
        SELECT s FROM Subcategoria s
        WHERE LOWER(s.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
        OR LOWER(s.categoria.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
        OR CAST(s.id AS string) LIKE %:texto%
    """)
    List<Subcategoria> buscarSubcategoriaAdmin(@Param("texto") String texto);
}