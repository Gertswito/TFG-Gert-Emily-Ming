package com.tfg.egm.repository;

import com.tfg.egm.entity.Cliente;
import com.tfg.egm.entity.Direccion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositorio JPA para la entidad Direccion.
 * Proporciona métodos para consultar, buscar y filtrar direcciones.
 */
public interface DireccionRepository extends JpaRepository<Direccion, Long> {

    /**
     * Busca todas las direcciones asociadas a un cliente.
     * @param cliente cliente del que se quieren obtener las direcciones
     * @return lista de direcciones del cliente
     */
    List<Direccion> findByCliente(Cliente cliente);

    /**
     * Busca direcciones para administración filtrando por varios campos.
     * @param texto texto de búsqueda
     * @return lista de direcciones encontradas
     */
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