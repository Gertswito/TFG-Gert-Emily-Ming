package com.tfg.egm.service;

import com.tfg.egm.entity.Cliente;
import com.tfg.egm.entity.Direccion;
import com.tfg.egm.repository.DireccionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DireccionService {

    private final DireccionRepository direccionRepository;

    public DireccionService(DireccionRepository direccionRepository) {
        this.direccionRepository = direccionRepository;
    }

    public List<Direccion> obtenerDirecciones() {
        return direccionRepository.findAll();
    }

    public List<Direccion> obtenerDirecciones(Cliente cliente) {
        return direccionRepository.findByCliente(cliente);
    }

    public Direccion save(Direccion direccion) {
        return direccionRepository.save(direccion);
    }

    public Optional<Direccion> actualizarDireccion(Long id, Direccion nuevaDireccion) {
        return direccionRepository.findById(id).map(direccion -> {
            direccion.setDireccion(nuevaDireccion.getDireccion()); 
            direccion.setLocalidad(nuevaDireccion.getLocalidad()); 
            direccion.setCodigoPostal(nuevaDireccion.getCodigoPostal()); 
            direccion.setComunidadAutonoma(nuevaDireccion.getComunidadAutonoma()); 
            return direccionRepository.save(direccion); 
        });
    }
    
    public void deleteDireccion(Long id) {
        direccionRepository.deleteById(id);
    }
}