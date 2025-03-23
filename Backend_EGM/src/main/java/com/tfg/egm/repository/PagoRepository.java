package com.tfg.egm.repository;

import com.tfg.egm.entity.Cliente;
import com.tfg.egm.entity.Pago;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    List <Pago> findByCliente(Cliente cliente);
}   