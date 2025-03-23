package com.tfg.egm.service;

import com.tfg.egm.entity.Direccion;
import com.tfg.egm.repository.DireccionRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    
    public void deleteDireccion(Long id) {
        if (!direccionRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "direccionNoExiste");
        }
        try {
            direccionRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "direccionNoSePuedeEliminar", e);
        }
    }
}