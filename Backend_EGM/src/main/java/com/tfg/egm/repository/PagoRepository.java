package com.tfg.egm.repository;

import com.tfg.egm.entity.Cliente;
import com.tfg.egm.entity.Pago;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositorio JPA para la entidad Pago.
 * Proporciona métodos para consultar, buscar y filtrar métodos de pago.
 */
public interface PagoRepository extends JpaRepository<Pago, Long> {

    /**
     * Busca todos los métodos de pago asociados a un cliente.
     * @param cliente cliente del que se quieren obtener los métodos de pago
     * @return lista de métodos de pago del cliente
     */
    List<Pago> findByCliente(Cliente cliente);

    /**
     * Busca métodos de pago para administración filtrando por varios campos.
     * @param texto texto de búsqueda
     * @return lista de métodos de pago encontrados
     */
    @Query(value = """
        SELECT p.* FROM pago p
        JOIN cliente c ON p.cliente_id = c.id
        WHERE CAST(p.id AS CHAR) LIKE CONCAT('%', :texto, '%')
        OR CAST(p.numero_tarjeta AS CHAR) LIKE CONCAT('%', :texto, '%')
        OR CAST(p.cvv AS CHAR) LIKE CONCAT('%', :texto, '%')
        OR LOWER(c.usuario) LIKE LOWER(CONCAT('%', :texto, '%'))
        OR DATE_FORMAT(p.fecha_caducidad, '%m-%Y') LIKE CONCAT('%', :texto, '%')
        """, nativeQuery = true)
    List<Pago> buscarPagoAdmin(@Param("texto") String texto);
}   