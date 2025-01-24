package com.tfg.egm.service;

import com.tfg.egm.entity.LineasVentas;
import com.tfg.egm.repository.LineasVentasRepository;
import org.springframework.stereotype.Service;

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
}