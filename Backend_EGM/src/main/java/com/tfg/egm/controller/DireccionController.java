package com.tfg.egm.controller;

import com.tfg.egm.entity.Cliente;
import com.tfg.egm.entity.Direccion;
import com.tfg.egm.service.ClienteService;
import com.tfg.egm.service.DireccionService;

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
import java.util.Optional;

/**
 * Controlador REST para gestionar las direcciones.
 * Permite obtener, buscar, crear, actualizar, eliminar y habilitar/deshabilitar direcciones.
 */
@RestController
public class DireccionController {

    private final DireccionService direccionService;
    private final ClienteService clienteService;

    /**
     * Constructor que inyecta los servicios de dirección y cliente.
     * @param direccionService servicio de direcciones
     * @param clienteService servicio de clientes
     */
    public DireccionController(DireccionService direccionService, ClienteService clienteService) {
        this.direccionService = direccionService;
        this.clienteService = clienteService;
    }

    /**
     * Obtiene la lista de todas las direcciones.
     * @return lista de direcciones
     */
    @GetMapping("/direcciones/all")
    public List<Direccion> obtenerDirecciones() {
        return direccionService.obtenerDirecciones();
    }

    /**
     * Obtiene una dirección por su ID.
     * @param id identificador de la dirección
     * @return ResponseEntity con la dirección o 404 si no se encuentra
     */
    @GetMapping("/direcciones/find/{id}")
    public ResponseEntity<Direccion> obtenerDireccionPorId(@PathVariable Long id) {
        try {
            Direccion direccion = direccionService.obtenerDireccionPorId(id);
            return ResponseEntity.ok(direccion);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene todas las direcciones de un cliente por su usuario.
     * @param user nombre de usuario
     * @return lista de direcciones del cliente
     */
    @GetMapping("/direcciones/cliente/{user}")
    public List<Direccion> obtenerDireccionesPorCliente(@PathVariable String user) {
        return direccionService.obtenerDirecciones(clienteService.obtenerClientePorUsuario(user));
    }

    /**
     * Busca direcciones para administración.
     * @param texto texto de búsqueda
     * @return ResponseEntity con la lista de direcciones encontradas o 404
     */
    @GetMapping("/direcciones/admin-busqueda/{texto}")
    public ResponseEntity<List<Direccion>> obtenerDireccionFiltroAdmin(@PathVariable String texto) {
        try {
            List<Direccion> direcciones = direccionService.buscarDireccionAdmin(texto);
            return ResponseEntity.ok(direcciones);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crea una nueva dirección para un cliente.
     * @param user nombre de usuario
     * @param direccion objeto dirección a crear
     * @return ResponseEntity con la nueva dirección
     */
    @PostMapping("/direcciones/new/{user}")
    public ResponseEntity<Direccion> crearDireccion(@PathVariable String user, @RequestBody Direccion direccion) {
        Cliente cliente = clienteService.obtenerClientePorUsuario(user);
        direccion.setCliente(cliente);
        Direccion nuevaDireccion = direccionService.save(direccion);
        return ResponseEntity.ok(nuevaDireccion);
    }

    /**
     * Actualiza una dirección existente.
     * @param id identificador de la dirección
     * @param direccion objeto dirección con los nuevos datos
     * @return ResponseEntity con la dirección actualizada o 404 si no se encuentra
     */
    @PutMapping("/direcciones/update/{id}")
    public ResponseEntity<Direccion> actualizarDireccion(@PathVariable Long id, @RequestBody Direccion direccion) {
        Optional<Direccion> direccionActualizado = direccionService.actualizarDireccion(id, direccion);
        return direccionActualizado.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    /**
     * Elimina una dirección por su ID.
     * @param id identificador de la dirección
     * @return ResponseEntity sin contenido o 404 si no se encuentra
     */
    @DeleteMapping("/direcciones/delete/{id}")
    public ResponseEntity<Void> deleteDireccion(@PathVariable Long id) {
        try {
            direccionService.deleteDireccion(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deshabilita una dirección (la marca como inactiva).
     * @param id identificador de la dirección
     * @return ResponseEntity sin contenido o 404 si no se encuentra
     */
    @DeleteMapping("/direcciones/disable/{id}")
    public ResponseEntity<Void> disableDireccion(@PathVariable Long id) {
        try {
            direccionService.disableDireccion(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Habilita una dirección (la marca como activa).
     * @param id identificador de la dirección
     * @return ResponseEntity sin contenido o 404 si no se encuentra
     */
    @DeleteMapping("/direcciones/enable/{id}")
    public ResponseEntity<Void> enableDireccion(@PathVariable Long id) {
        try {
            direccionService.enableDireccion(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}