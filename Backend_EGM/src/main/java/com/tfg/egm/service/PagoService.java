package com.tfg.egm.service;

import com.tfg.egm.entity.Cliente;
import com.tfg.egm.entity.Pago;
import com.tfg.egm.repository.PagoRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de métodos de pago.
 * Proporciona métodos para obtener, buscar, crear, actualizar, eliminar y habilitar/deshabilitar métodos de pago.
 */
@Service
public class PagoService {

    private final PagoRepository pagoRepository;

    /**
     * Constructor que inyecta el repositorio de pagos.
     * @param pagoRepository repositorio de pagos
     */
    public PagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    /**
     * Obtiene la lista de todos los métodos de pago.
     * @return lista de métodos de pago
     */
    public List<Pago> obtenerPagos() {
        return pagoRepository.findAll();
    }

    /**
     * Obtiene un método de pago por su ID.
     * @param id identificador del pago
     * @return el pago encontrado
     * @throws ResponseStatusException si no existe el pago
     */
    public Pago obtenerPagoPorId(Long id) {
        return pagoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "pagoNoExiste"));
    }

    /**
     * Obtiene todos los métodos de pago asociados a un cliente.
     * @param cliente cliente del que se quieren obtener los métodos de pago
     * @return lista de métodos de pago del cliente
     */
    public List<Pago> obtenerPagos(Cliente cliente) {
        return pagoRepository.findByCliente(cliente);
    }

    /**
     * Busca métodos de pago para administración filtrando por varios campos.
     * @param texto texto de búsqueda
     * @return lista de métodos de pago encontrados
     */
    public List<Pago> buscarPagoAdmin(String texto) {
        return pagoRepository.buscarPagoAdmin(texto);
    }

    /**
     * Guarda un nuevo método de pago.
     * @param pago objeto pago a guardar
     * @return el pago guardado
     */
    public Pago save(Pago pago) {
        return pagoRepository.save(pago);
    }
    
    /**
     * Actualiza un método de pago existente.
     * @param id identificador del pago
     * @param nuevoPago objeto pago con los nuevos datos
     * @return Optional con el pago actualizado o vacío si no se encuentra
     */
    public Optional<Pago> actualizarPago(Long id, Pago nuevoPago) {
        return pagoRepository.findById(id).map(pago -> {
            pago.setCvv(nuevoPago.getCvv()); 
            pago.setNumeroTarjeta(nuevoPago.getNumeroTarjeta());
            pago.setFechaCaducidad(nuevoPago.getFechaCaducidad()); 
            if (nuevoPago.getActivo() != null) {
                pago.setActivo(nuevoPago.getActivo()); 
            }
            if (nuevoPago.getCliente() != pago.getCliente()) {
                pago.setCliente(nuevoPago.getCliente()); 
            }
            return pagoRepository.save(pago);
        });
    }
    
    /**
     * Elimina un método de pago por su ID.
     * @param id identificador del pago
     * @throws ResponseStatusException si no existe el pago o no se puede eliminar
     */
    public void deletePago(Long id) {
        if (!pagoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "pagoNoExiste");
        }
        try {
            pagoRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "pagoNoSePuedeEliminar", e);
        }
    }

    /**
     * Deshabilita un método de pago (lo marca como inactivo).
     * @param id identificador del pago
     * @throws ResponseStatusException si no existe el pago o no se puede actualizar
     */
    public void disablePago(Long id) {
        if (!pagoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "pagoNoExiste");
        }
        try {
            Pago pago = pagoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "pagoNoEncontrado"));
            pago.setActivo(false);
            pagoRepository.save(pago);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "errorInesperadoSetFalse", e);
        }
    }

    /**
     * Habilita un método de pago (lo marca como activo).
     * @param id identificador del pago
     * @throws ResponseStatusException si no existe el pago o no se puede actualizar
     */
    public void enablePago(Long id) {
        if (!pagoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "pagoNoExiste");
        }
        try {
            Pago pago = pagoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "pagoNoEncontrado"));
            pago.setActivo(true);
            pagoRepository.save(pago);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "errorInesperadoSetTrue", e);
        }
    }
}