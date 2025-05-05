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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class DireccionController {

    private final DireccionService direccionService;
    private final ClienteService clienteService;

    public DireccionController(DireccionService direccionService, ClienteService clienteService) {
        this.direccionService = direccionService;
        this.clienteService = clienteService;
    }

    @GetMapping("/direcciones/all")
    public List<Direccion> obtenerDirecciones() {
        return direccionService.obtenerDirecciones();
    }

    @GetMapping("/direcciones/find/{id}")
    public ResponseEntity<Direccion> obtenerDireccionPorId(@PathVariable Long id) {
        try {
            Direccion direccion = direccionService.obtenerDireccionPorId(id);
            return ResponseEntity.ok(direccion);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/direcciones/cliente/{user}")
    public List<Direccion> obtenerDireccionesPorCliente(@PathVariable String user) {
        return direccionService.obtenerDirecciones(clienteService.obtenerClientePorUsuario(user));
    }

    @PostMapping("/direcciones/new/{user}")
    public ResponseEntity<Direccion> crearDireccion(@PathVariable String user, @RequestBody Direccion direccion) {
        Cliente cliente = clienteService.obtenerClientePorUsuario(user);
        direccion.setCliente(cliente);
        Direccion nuevaDireccion = direccionService.save(direccion);
        return ResponseEntity.ok(nuevaDireccion);
    }

    @PutMapping("/direcciones/update/{id}")
    public ResponseEntity<Direccion> actualizarDireccion(@PathVariable Long id, @RequestBody Direccion direccion) {
        Optional<Direccion> direccionActualizado = direccionService.actualizarDireccion(id, direccion);
        return direccionActualizado.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/direcciones/delete/{id}")
    public ResponseEntity<Void> deleteDireccion(@PathVariable Long id) {
        try {
            direccionService.deleteDireccion(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/direcciones/disable/{id}")
    public ResponseEntity<Void> disableDireccion(@PathVariable Long id) {
        try {
            direccionService.disableDireccion(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

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