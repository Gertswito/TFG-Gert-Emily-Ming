package com.tfg.egm.service;

import com.tfg.egm.entity.Direccion;
import com.tfg.egm.repository.DireccionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DireccionService {

    private final DireccionRepository direccionRepository;

    public DireccionService(DireccionRepository direccionRepository) {
        this.direccionRepository = direccionRepository;
    }

    public List<Direccion> obtenerDirecciones() {
        return direccionRepository.findAll();
    }
}