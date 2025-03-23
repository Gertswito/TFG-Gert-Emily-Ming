package com.tfg.egm.service;

import com.tfg.egm.entity.Pago;
import com.tfg.egm.repository.PagoRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;

    public PagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    public List<Pago> obtenerPagos() {
        return pagoRepository.findAll();
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
}