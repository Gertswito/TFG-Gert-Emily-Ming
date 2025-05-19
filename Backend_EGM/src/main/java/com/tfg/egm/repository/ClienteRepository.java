package com.tfg.egm.repository;

import com.tfg.egm.entity.Cliente;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositorio JPA para la entidad Cliente.
 * Proporciona métodos para consultar, buscar y comprobar la existencia de clientes.
 */
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * Comprueba si existe un cliente por su usuario.
     * @param usuario nombre de usuario
     * @return true si existe, false si no
     */
    boolean existsByUsuario(String usuario);

    /**
     * Comprueba si existe un cliente por su DNI.
     * @param dni DNI del cliente
     * @return true si existe, false si no
     */
    boolean existsByDni(String dni);

    /**
     * Comprueba si existe un cliente por su email.
     * @param email email del cliente
     * @return true si existe, false si no
     */
    boolean existsByEmail(String email);

    /**
     * Busca un cliente por su usuario.
     * @param usuario nombre de usuario
     * @return cliente encontrado o null
     */
    Cliente findByUsuario(String usuario);

    /**
     * Busca un cliente por su ID.
     * @param id identificador del cliente
     * @return cliente encontrado o null
     */
    Cliente findById(int id);

    /**
     * Busca clientes para administración filtrando por varios campos.
     * @param texto texto de búsqueda
     * @return lista de clientes encontrados
     */
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