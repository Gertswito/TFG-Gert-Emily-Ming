package com.tfg.egm.controller;

import com.tfg.egm.entity.Cliente;
import com.tfg.egm.service.ClienteService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/clientes/all")
    public List<Cliente> obtenerClientes() {
        return clienteService.obtenerClientes();
    }

    @GetMapping("/clientes/usuario/{usuario}")
    public Cliente obtenerCliente(@PathVariable String usuario) {
        return clienteService.obtenerClientePorUsuario(usuario);
    }

    @PostMapping("/clientes/new")
    public ResponseEntity<Object> createCliente(@RequestBody Cliente cliente) throws URISyntaxException {
        if (cliente.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "clienteExiste");
        }
        try {
            Cliente nuevoCliente = clienteService.save(cliente);
            URI location = new URI("/clientes/new/" + nuevoCliente.getId());
            return ResponseEntity.created(location).body(nuevoCliente);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("error", ex.getReason()));
        }
    }

    @PostMapping("/clientes/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Cliente cliente) {
        String token = clienteService.login(cliente);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/clientes/update")
    public ResponseEntity<Cliente> actualizarCliente(@RequestBody Cliente cliente) throws URISyntaxException {
        try {
            Cliente clienteActualizado = clienteService.actualizarCliente(cliente);
            return ResponseEntity.ok(clienteActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/clientes/delete/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        try {
            clienteService.deleteCliente(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}