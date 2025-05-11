package com.tfg.egm.repository;

import com.tfg.egm.entity.Cliente;
import com.tfg.egm.entity.Direccion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DireccionRepository extends JpaRepository<Direccion, Long> {
    List<Direccion> findByCliente(Cliente cliente);
    @Query("""
        SELECT d FROM Direccion d
        WHERE (
            STR(d.id) LIKE CONCAT('%', :texto, '%') OR
            LOWER(d.direccion) LIKE LOWER(CONCAT('%', :texto, '%')) OR
            STR(d.codigoPostal) LIKE CONCAT('%', :texto, '%') OR
            LOWER(d.localidad) LIKE LOWER(CONCAT('%', :texto, '%')) OR
            LOWER(d.comunidadAutonoma) LIKE LOWER(CONCAT('%', :texto, '%')) OR
            LOWER(d.cliente.usuario) LIKE LOWER(CONCAT('%', :texto, '%'))
        )
    """)
    List<Direccion> buscarDireccionAdmin(@Param("texto") String texto);
}