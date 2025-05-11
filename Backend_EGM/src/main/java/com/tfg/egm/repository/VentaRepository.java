package com.tfg.egm.repository;

import com.tfg.egm.entity.Venta;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    @Query(value = """
        SELECT v.* FROM venta v
        JOIN cliente c ON v.cliente_id = c.id
        JOIN direccion d ON v.direccion_id = d.id
        JOIN pago p ON v.pago_id = p.id
        WHERE CAST(v.id AS CHAR) LIKE CONCAT('%', :texto, '%')
        OR CAST(v.precio_final AS CHAR) LIKE CONCAT('%', :texto, '%')
        OR LOWER(c.usuario) LIKE LOWER(CONCAT('%', :texto, '%'))
        OR LOWER(CONCAT(d.direccion, ', ', d.localidad, ', ', d.comunidad_autonoma)) LIKE LOWER(CONCAT('%', :texto, '%'))
        OR CAST(p.numero_tarjeta AS CHAR) LIKE CONCAT('%', :texto, '%')
        OR DATE_FORMAT(v.fecha_hora, '%d-%m-%Y %H:%i') LIKE CONCAT('%', :texto, '%')
        """, nativeQuery = true)
    List<Venta> buscarVentaAdmin(@Param("texto") String texto);
}