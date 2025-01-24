package com.tfg.egm.controller;

import com.tfg.egm.entity.Cliente;
import com.tfg.egm.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.net.URISyntaxException;
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
    public ResponseEntity<Object> loginCliente(@RequestBody Cliente cliente) throws URISyntaxException {    
        try {
            Cliente usuario = clienteService.login(cliente);
            URI location = new URI("/clientes/new/" + usuario.getId());
            return ResponseEntity.created(location).body(usuario);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("error", ex.getReason()));
        }
    }
}