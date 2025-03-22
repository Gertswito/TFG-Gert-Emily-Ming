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
            direccion.setDireccion(nuevaDireccion.getDireccion()); // Actualiza la dirección
            direccion.setLocalidad(nuevaDireccion.getLocalidad()); // Actualiza la localidad
            direccion.setCodigoPostal(nuevaDireccion.getCodigoPostal()); // Actualiza el código postal
            direccion.setComunidadAutonoma(nuevaDireccion.getComunidadAutonoma()); // Actualiza la comunidad autónoma
            return direccionRepository.save(direccion); // Guarda la dirección actualizada
        });
    }
    
    public void deleteDireccion(Long id) {
        direccionRepository.deleteById(id);
    }
}