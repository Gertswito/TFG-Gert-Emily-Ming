package com.tfg.egm.repository;

import com.tfg.egm.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByUsuario(String usuario);
    boolean existsByDni(String dni);
    boolean existsByEmail(String email);
    Cliente findByUsuario(String usuario);
}