package com.tfg.egm.service;

import com.tfg.egm.entity.Cliente;
import com.tfg.egm.entity.Direccion;
import com.tfg.egm.entity.Pago;
import com.tfg.egm.repository.PagoRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public Pago obtenerPagoPorId(Long id) {
        return pagoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "pagoNoExiste"));
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
            if (nuevoPago.getActivo() != null) {
                pago.setActivo(nuevoPago.getActivo()); 
            }
            if (nuevoPago.getCliente() != pago.getCliente()) {
                pago.setCliente(nuevoPago.getCliente()); 
            }
            return pagoRepository.save(pago);
        });
    }
    
    public void deletePago(Long id) {
        if (!pagoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "pagoNoExiste");
        }
        try {
            pagoRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "pagoNoSePuedeEliminar", e);
        }
    }

    public void disablePago(Long id) {
        if (!pagoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "pagoNoExiste");
        }
        try {
            Pago pago = pagoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "pagoNoEncontrado"));
            pago.setActivo(false);
            pagoRepository.save(pago);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "errorInesperadoSetFalse", e);
        }
    }

    public void enablePago(Long id) {
        if (!pagoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "pagoNoExiste");
        }
        try {
            Pago pago = pagoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "pagoNoEncontrado"));
            pago.setActivo(true);
            pagoRepository.save(pago);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "errorInesperadoSetTrue", e);
        }
    }
}