package com.tfg.egm.service;

import com.tfg.egm.entity.Categoria;
import com.tfg.egm.entity.Venta;
import com.tfg.egm.repository.VentaRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;

    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    public List<Venta> obtenerVentas() {
        return ventaRepository.findAll();
    }

    public void deleteVenta(Long id) {
        if (!ventaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ventaNoExiste");
        }
        try {
            ventaRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "ventaNoSePuedeEliminar", e);
        }
    }

    public Venta save(Venta venta) {
        if(venta.getFechaHora() != null) {
            LocalDateTime fechaHoy = LocalDateTime.now();
            if (venta.getFechaHora().isAfter(fechaHoy)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fechaInvalida");
            }
        }
        return ventaRepository.save(venta);
    }
}