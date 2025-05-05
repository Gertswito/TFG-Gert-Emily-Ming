package com.tfg.egm.service;

import com.tfg.egm.entity.Cliente;
import com.tfg.egm.entity.Direccion;
import com.tfg.egm.repository.DireccionRepository;
import com.tfg.egm.repository.VentaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class DireccionService {

    private final VentaRepository ventaRepository;

    private final DireccionRepository direccionRepository;

    public DireccionService(DireccionRepository direccionRepository, VentaRepository ventaRepository) {
        this.direccionRepository = direccionRepository;
        this.ventaRepository = ventaRepository;
    }

    public List<Direccion> obtenerDirecciones() {
        return direccionRepository.findAll();
    }

    public Direccion obtenerDireccionPorId(Long id) {
        return direccionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "direccionNoExiste"));
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
            if(nuevaDireccion.getActivo() != null) {
                direccion.setActivo(nuevaDireccion.getActivo()); 
            }
            if (nuevaDireccion.getCliente() != direccion.getCliente()) {
                direccion.setCliente(nuevaDireccion.getCliente()); 
            }
            return direccionRepository.save(direccion); 
        });
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

    public void disableDireccion(Long id) {
        if (!direccionRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "direccionNoExiste");
        }
        try {
            Direccion direccion = direccionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "direccionNoEncontrada"));
            direccion.setActivo(false);
            direccionRepository.save(direccion);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "errorInesperadoSetFalse", e);
        }
    }

    public void enableDireccion(Long id) {
        if (!direccionRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "direccionNoExiste");
        }
        try {
            Direccion direccion = direccionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "direccionNoEncontrada"));
            direccion.setActivo(true);
            direccionRepository.save(direccion);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "errorInesperadoSetTrue", e);
        }
    }
}