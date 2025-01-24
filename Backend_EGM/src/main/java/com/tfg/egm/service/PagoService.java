package com.tfg.egm.service;

import com.tfg.egm.entity.Pago;
import com.tfg.egm.repository.PagoRepository;
import org.springframework.stereotype.Service;

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
}