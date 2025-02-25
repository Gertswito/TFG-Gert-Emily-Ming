package com.tfg.egm.repository;

import com.tfg.egm.entity.Producto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findBySubcategoriaId(int id);
}
