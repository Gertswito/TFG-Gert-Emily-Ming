package com.tfg.egm.service;

import com.tfg.egm.entity.LineasVentas;
import com.tfg.egm.entity.Subcategoria;
import com.tfg.egm.repository.LineasVentasRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class LineasVentasService {

    private final LineasVentasRepository lineasVentasRepository;

    public LineasVentasService(LineasVentasRepository lineasVentasRepository) {
        this.lineasVentasRepository = lineasVentasRepository;
    }

    public List<LineasVentas> obtenerLineasVentas() {
        return lineasVentasRepository.findAll();
    }

    public LineasVentas obtenerLineaVentaPorId(Long id) {
        return lineasVentasRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "subcategoriaNoExiste"));
    }

    public void deleteLineasVentas(Long id) {
        if (!lineasVentasRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "lineasVentaNoExiste");
        }
        try {
            lineasVentasRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "lineasVentaNoSePuedeEliminar", e);
        }
    }

    public List<LineasVentas> buscarLineaVentaAdmin(String texto) {
        return lineasVentasRepository.buscarLineaVentaAdmin(texto);
    }

    public LineasVentas save(LineasVentas lineasVentas) {
        return lineasVentasRepository.save(lineasVentas);
    }

    public LineasVentas update(LineasVentas lineasVentas) {
        return lineasVentasRepository.save(lineasVentas);
    }
}