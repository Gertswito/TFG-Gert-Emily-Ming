package com.tfg.egm.service;

import com.tfg.egm.entity.Cliente;
import com.tfg.egm.repository.ClienteRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public ClienteService(ClienteRepository clienteRepository, BCryptPasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Cliente> obtenerClientes() {
        return clienteRepository.findAll();
    }

    public Cliente save(Cliente cliente) {
        if (clienteRepository.existsByUsuario(cliente.getUsuario())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombreUsuarioExiste");
        }
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "emailExiste");
        }
        if (clienteRepository.existsByDni(cliente.getDni())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dniExiste");
        }
        LocalDate fechaHoy = LocalDate.now();
        if (cliente.getFechaNac().isAfter(fechaHoy)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fechaInvalida");
        }
    
        if (cliente.getRol() == null) {
            cliente.setRol("USER");
        }

        BCryptPasswordEncoder newPasswordEncoder = new BCryptPasswordEncoder();
        cliente.setContrasenha(newPasswordEncoder.encode(cliente.getContrasenha()));

        return clienteRepository.save(cliente);
    }

    public Cliente login(Cliente cliente) {
        if (!clienteRepository.existsByUsuario(cliente.getUsuario())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "usuarioNoExiste");
        }
        Cliente usuario = clienteRepository.findByUsuario(cliente.getUsuario());
    
        if (!passwordEncoder.matches(cliente.getContrasenha(), usuario.getContrasenha())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "contrasenhaIncorrecta");
        }

        return usuario;
    }

    public Cliente actualizarCliente(Cliente cliente) {
        if (cliente.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "idRequerido");
        }
        
        Cliente clienteExistente = clienteRepository.findByUsuario(cliente.getUsuario());
    
        // Validar que el email y DNI no estén en uso por otro cliente
        if (!clienteExistente.getEmail().equals(cliente.getEmail()) &&
            clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "emailExiste");
        }
    
        if (!clienteExistente.getDni().equals(cliente.getDni()) &&
            clienteRepository.existsByDni(cliente.getDni())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dniExiste");
        }
    
        // Validar que la fecha de nacimiento no sea futura
        LocalDate fechaHoy = LocalDate.now();
        if (cliente.getFechaNac().isAfter(fechaHoy)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fechaInvalida");
        }
    
        // Mantener el rol o asignarlo por defecto
        if (cliente.getRol() == null) {
            cliente.setRol(clienteExistente.getRol());
        }
    
        // Mantener la contraseña encriptada si no fue cambiada
        if (cliente.getContrasenha() != null && !cliente.getContrasenha().equals(clienteExistente.getContrasenha())) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            cliente.setContrasenha(passwordEncoder.encode(cliente.getContrasenha()));
        }else {
            cliente.setContrasenha(clienteExistente.getContrasenha());
        }
    
        // Mantener direcciones y pagos
        cliente.setDirecciones(clienteExistente.getDirecciones());
        cliente.setPagos(clienteExistente.getPagos());
    
        return clienteRepository.save(cliente);
    }
}