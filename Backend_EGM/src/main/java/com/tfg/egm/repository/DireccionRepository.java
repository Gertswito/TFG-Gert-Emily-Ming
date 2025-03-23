package com.tfg.egm.repository;

import com.tfg.egm.entity.Cliente;
import com.tfg.egm.entity.Direccion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DireccionRepository extends JpaRepository<Direccion, Long> {
    List<Direccion> findByCliente(Cliente cliente);
}