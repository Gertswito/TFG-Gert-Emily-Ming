package com.tfg.egm.repository;

import com.tfg.egm.entity.Cliente;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByUsuario(String usuario);
    boolean existsByDni(String dni);
    boolean existsByEmail(String email);
    Cliente findByUsuario(String usuario);
    Cliente findById(int id);
    @Query(value = """
        SELECT * FROM cliente c
        WHERE CAST(c.id AS CHAR) LIKE CONCAT('%', :texto, '%')
        OR LOWER(c.rol) LIKE LOWER(CONCAT('%', :texto, '%'))
        OR LOWER(c.dni) LIKE LOWER(CONCAT('%', :texto, '%'))
        OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
        OR LOWER(c.apellidos) LIKE LOWER(CONCAT('%', :texto, '%'))
        OR LOWER(c.usuario) LIKE LOWER(CONCAT('%', :texto, '%'))
        OR LOWER(c.email) LIKE LOWER(CONCAT('%', :texto, '%'))
        OR LOWER(c.telefono) LIKE LOWER(CONCAT('%', :texto, '%'))
        OR DATE_FORMAT(c.fecha_nac, '%d-%m-%Y') LIKE CONCAT('%', :texto, '%')
    """, nativeQuery = true)
    List<Cliente> buscarClienteAdmin(@Param("texto") String texto);
}