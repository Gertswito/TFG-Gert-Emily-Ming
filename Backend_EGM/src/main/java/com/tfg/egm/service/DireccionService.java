package com.tfg.egm.service;

import com.tfg.egm.entity.Cliente;
import com.tfg.egm.entity.Direccion;
import com.tfg.egm.repository.DireccionRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de direcciones.
 * Proporciona métodos para obtener, buscar, crear, actualizar, eliminar y habilitar/deshabilitar direcciones.
 */
@Service
public class DireccionService {

    private final DireccionRepository direccionRepository;

    /**
     * Constructor que inyecta el repositorio de direcciones.
     * @param direccionRepository repositorio de direcciones
     */
    public DireccionService(DireccionRepository direccionRepository) {
        this.direccionRepository = direccionRepository;
    }

    /**
     * Obtiene la lista de todas las direcciones.
     * @return lista de direcciones
     */
    public List<Direccion> obtenerDirecciones() {
        return direccionRepository.findAll();
    }

    /**
     * Obtiene una dirección por su ID.
     * @param id identificador de la dirección
     * @return la dirección encontrada
     * @throws ResponseStatusException si no existe la dirección
     */
    public Direccion obtenerDireccionPorId(Long id) {
        return direccionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "direccionNoExiste"));
    }

    /**
     * Obtiene todas las direcciones asociadas a un cliente.
     * @param cliente cliente del que se quieren obtener las direcciones
     * @return lista de direcciones del cliente
     */
    public List<Direccion> obtenerDirecciones(Cliente cliente) {
        return direccionRepository.findByCliente(cliente);
    }

    /**
     * Busca direcciones para administración filtrando por varios campos.
     * @param texto texto de búsqueda
     * @return lista de direcciones encontradas
     */
    public List<Direccion> buscarDireccionAdmin(String texto) {
        return direccionRepository.buscarDireccionAdmin(texto);
    }

    /**
     * Guarda una nueva dirección.
     * @param direccion objeto dirección a guardar
     * @return la dirección guardada
     */
    public Direccion save(Direccion direccion) {
        return direccionRepository.save(direccion);
    }

    /**
     * Actualiza una dirección existente.
     * @param id identificador de la dirección
     * @param nuevaDireccion objeto dirección con los nuevos datos
     * @return Optional con la dirección actualizada o vacío si no se encuentra
     */
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
    
    /**
     * Elimina una dirección por su ID.
     * @param id identificador de la dirección
     * @throws ResponseStatusException si no existe la dirección o no se puede eliminar
     */
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

    /**
     * Deshabilita una dirección (la marca como inactiva).
     * @param id identificador de la dirección
     * @throws ResponseStatusException si no existe la dirección o no se puede actualizar
     */
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

    /**
     * Habilita una dirección (la marca como activa).
     * @param id identificador de la dirección
     * @throws ResponseStatusException si no existe la dirección o no se puede actualizar
     */
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