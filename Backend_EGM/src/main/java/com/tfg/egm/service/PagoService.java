package com.tfg.egm.service;

import com.tfg.egm.entity.Cliente;
import com.tfg.egm.entity.Pago;
import com.tfg.egm.repository.PagoRepository;
import org.springframework.stereotype.Service;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;

    public PagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    public List<Pago> obtenerPagos() {
        return pagoRepository.findAll();
    }

    public List<Pago> obtenerPagos(Cliente cliente) {
        return pagoRepository.findByCliente(cliente);
    }

    public Pago save(Pago pago) {
        return pagoRepository.save(pago);
    }
    
    public Optional<Pago> actualizarPago(Long id, Pago nuevoPago) {
        return pagoRepository.findById(id).map(pago -> {
            pago.setCvv(nuevoPago.getCvv()); 
            pago.setNumeroTarjeta(nuevoPago.getNumeroTarjeta());
            pago.setFechaCaducidad(nuevoPago.getFechaCaducidad()); 
            return pagoRepository.save(pago);
        });
    }

    public void deletePago(Long id) {
        pagoRepository.deleteById(id);
    }
}