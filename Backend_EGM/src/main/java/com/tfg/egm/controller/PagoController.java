package com.tfg.egm.controller;

import com.tfg.egm.entity.Cliente;
import com.tfg.egm.entity.Pago;
import com.tfg.egm.service.ClienteService;
import com.tfg.egm.service.PagoService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST para gestionar los métodos de pago.
 * Permite obtener, buscar, crear, actualizar, eliminar y habilitar/deshabilitar métodos de pago.
 */
@RestController
public class PagoController {

    private final PagoService pagoService;
    private final ClienteService clienteService;

    /**
     * Constructor que inyecta los servicios de pago y cliente.
     * @param pagoService servicio de pagos
     * @param clienteService servicio de clientes
     */
    public PagoController(PagoService pagoService , ClienteService clienteService) {
        this.pagoService = pagoService;
        this.clienteService = clienteService;
    }

    /**
     * Obtiene la lista de todos los métodos de pago.
     * @return lista de métodos de pago
     */
    @GetMapping("/pagos/all")
    public List<Pago> obtenerPagos() {
        return pagoService.obtenerPagos();
    }

    /**
     * Obtiene un método de pago por su ID.
     * @param id identificador del pago
     * @return ResponseEntity con el pago o 404 si no se encuentra
     */
    @GetMapping("/pagos/find/{id}")
    public ResponseEntity<Pago> obtenerPagoPorId(@PathVariable Long id) {
        try {
            Pago pago = pagoService.obtenerPagoPorId(id);
            return ResponseEntity.ok(pago);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene todos los métodos de pago de un cliente por su usuario.
     * @param user nombre de usuario
     * @return lista de métodos de pago del cliente
     */
    @GetMapping("/pagos/cliente/{user}")
    public List<Pago> obtenerPagosPorCliente(@PathVariable String user) {
        return pagoService.obtenerPagos(clienteService.obtenerClientePorUsuario(user));
    }

    /**
     * Busca métodos de pago para administración.
     * @param texto texto de búsqueda
     * @return ResponseEntity con la lista de pagos encontrados o 404
     */
    @GetMapping("/pagos/admin-busqueda/{texto}")
    public ResponseEntity<List<Pago>> obtenerPagosFiltroAdmin(@PathVariable String texto) {
        try {
            List<Pago> pagos = pagoService.buscarPagoAdmin(texto);
            return ResponseEntity.ok(pagos);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crea un nuevo método de pago para un cliente.
     * @param user nombre de usuario
     * @param pago objeto pago a crear
     * @return ResponseEntity con el nuevo pago
     */
    @PostMapping("/pagos/new/{user}")
    public ResponseEntity<Pago> crearPago(@PathVariable String user, @RequestBody Pago pago) {
        Cliente cliente = clienteService.obtenerClientePorUsuario(user);
        pago.setCliente(cliente);
        Pago nuevoPago = pagoService.save(pago);
        return ResponseEntity.ok(nuevoPago);
    }

    /**
     * Actualiza un método de pago existente.
     * @param id identificador del pago
     * @param pago objeto pago con los nuevos datos
     * @return ResponseEntity con el pago actualizado
     */
    @PutMapping("/pagos/update/{id}")
    public ResponseEntity<Pago> actualizarPago(@PathVariable Long id, @RequestBody Pago pago) {
        pagoService.actualizarPago(id, pago);
        return ResponseEntity.ok(pago);
    }
    
    /**
     * Elimina un método de pago por su ID.
     * @param id identificador del pago
     * @return ResponseEntity sin contenido o 404 si no se encuentra
     */
    @DeleteMapping("/pagos/delete/{id}")
    public ResponseEntity<Void> deletePago(@PathVariable Long id) {
        try {
            pagoService.deletePago(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deshabilita un método de pago (lo marca como inactivo).
     * @param id identificador del pago
     * @return ResponseEntity sin contenido o 404 si no se encuentra
     */
    @DeleteMapping("/pagos/disable/{id}")
    public ResponseEntity<Void> disablePago(@PathVariable Long id) {
        try {
            pagoService.disablePago(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Habilita un método de pago (lo marca como activo).
     * @param id identificador del pago
     * @return ResponseEntity sin contenido o 404 si no se encuentra
     */
    @DeleteMapping("/pagos/enable/{id}")
    public ResponseEntity<Void> enablePago(@PathVariable Long id) {
        try {
            pagoService.enablePago(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}