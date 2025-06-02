package com.tfg.egm.controller;

import com.tfg.egm.entity.Cliente;
import com.tfg.egm.service.ClienteService;
import com.tfg.egm.service.EmailService;

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

/**
 * Controlador REST para gestionar los clientes.
 * Permite obtener, buscar, crear, actualizar, eliminar clientes y login.
 */
/**
 * Controlador REST para gestionar los clientes.
 * Permite obtener, buscar, crear, actualizar, eliminar clientes y login.
 */
@RestController
public class ClienteController {

    private final ClienteService clienteService;
    
    private final EmailService emailService;

    /**
     * Constructor que inyecta el servicio de clientes y el servicio de email.
     * @param clienteService servicio de clientes
     * @param emailService servicio de envío de correos electrónicos
     */
    public ClienteController(ClienteService clienteService, EmailService emailService) {
        this.clienteService = clienteService;
        this.emailService = emailService;
    }

    /**
     * Obtiene la lista de todos los clientes.
     * @return lista de clientes
     */
    @GetMapping("/clientes/all")
    public List<Cliente> obtenerClientes() {
        return clienteService.obtenerClientes();
    }

    /**
     * Obtiene un cliente por su usuario.
     * @param usuario nombre de usuario
     * @return cliente encontrado
     */
    @GetMapping("/clientes/usuario/{usuario}")
    public Cliente obtenerCliente(@PathVariable String usuario) {
        return clienteService.obtenerClientePorUsuario(usuario);
    }

    /**
     * Obtiene un cliente por su ID.
     * @param id identificador del cliente
     * @return ResponseEntity con el cliente o 404 si no se encuentra
     */
    @GetMapping("/clientes/find/{id}")
    public ResponseEntity<Cliente> obtenerClientePorId(@PathVariable Long id) {
        try {
            Cliente cliente = clienteService.obtenerClientePorId(id);
            return ResponseEntity.ok(cliente);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Busca clientes para administración.
     * @param texto texto de búsqueda
     * @return ResponseEntity con la lista de clientes encontrados o 404
     */
    @GetMapping("/clientes/admin-busqueda/{texto}")
    public ResponseEntity<List<Cliente>> obtenerClienteFiltroAdmin(@PathVariable String texto) {
        try {
            List<Cliente> clientes = clienteService.buscarClienteAdmin(texto);
            return ResponseEntity.ok(clientes);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crea un nuevo cliente.
     * @param cliente objeto cliente a crear
     * @return ResponseEntity con el nuevo cliente y la ubicación
     * @throws URISyntaxException si la URI no es válida
     */
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

    /**
     * Crea un nuevo cliente y le manda un correo.
     * @param cliente objeto cliente a crear
     * @return ResponseEntity con el nuevo cliente y la ubicación
     * @throws URISyntaxException si la URI no es válida
     */
    @PostMapping("/clientes/registrar")
    public ResponseEntity<Object> registrarCliente(@RequestBody Cliente cliente) throws URISyntaxException {
        if (cliente.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "clienteExiste");
        }
        try {
            Cliente nuevoCliente = clienteService.save(cliente);
            String asunto = "Bienvenido a Fresma";
            String nombre = nuevoCliente.getNombre() + " " + nuevoCliente.getApellidos();
            String cuerpo = String.format("Hola %s,\n\nGracias por registrarte en Tienda Fresma. Tu cuenta ha sido creada correctamente y ya puede iniciar sesión.\n\nAtentamente, el equipo de Tienda Fresma", nombre);

            emailService.enviarCorreo(nuevoCliente.getEmail(), asunto, cuerpo);
            URI location = new URI("/clientes/new/" + nuevoCliente.getId());
            return ResponseEntity.created(location).body(nuevoCliente);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("error", ex.getReason()));
        }
    }

    /**
     * Login de cliente.
     * @param cliente objeto cliente con usuario y contraseña
     * @return ResponseEntity con el token generado
     */
    @PostMapping("/clientes/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Cliente cliente) {
        String token = clienteService.login(cliente);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza un cliente existente.
     * @param cliente objeto cliente con los nuevos datos
     * @return ResponseEntity con el cliente actualizado o error
     * @throws URISyntaxException si la URI no es válida
     */
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

    /**
     * Elimina un cliente por su ID.
     * @param id identificador del cliente
     * @return ResponseEntity sin contenido o 404 si no se encuentra
     */
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