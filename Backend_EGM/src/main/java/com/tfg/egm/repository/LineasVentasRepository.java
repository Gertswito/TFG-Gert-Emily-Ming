package com.tfg.egm.repository;

import com.tfg.egm.entity.LineasVentas;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LineasVentasRepository extends JpaRepository<LineasVentas, Long> {
    List<LineasVentas> findByVentaId(Long ventaId);
    @Query(value = """
        SELECT lv.* FROM lineasventa lv
        JOIN producto p ON lv.producto_id = p.id
        JOIN venta v ON lv.venta_id = v.id
        WHERE CAST(lv.id AS CHAR) LIKE CONCAT('%', :texto, '%')
        OR CAST(lv.cantidad_pedida AS CHAR) LIKE CONCAT('%', :texto, '%')
        OR CAST(lv.precio_unitario AS CHAR) LIKE CONCAT('%', :texto, '%')
        OR CAST(lv.precio_total AS CHAR) LIKE CONCAT('%', :texto, '%')
        OR LOWER(CONCAT(p.marca, ' ', p.nombre)) LIKE LOWER(CONCAT('%', :texto, '%'))
        OR LOWER(p.referencia) LIKE LOWER(CONCAT('%', :texto, '%'))
        OR CAST(v.id AS CHAR) LIKE CONCAT('%', :texto, '%')
        """, nativeQuery = true)
    List<LineasVentas> buscarLineaVentaAdmin(@Param("texto") String texto);
}