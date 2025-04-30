package com.tfg.egm.service;

import com.tfg.egm.entity.Cliente;
import com.tfg.egm.entity.Subcategoria;
import com.tfg.egm.repository.ClienteRepository;
import com.tfg.egm.security.JwtTokenUtil;

import org.springframework.dao.DataIntegrityViolationException;
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

    private final JwtTokenUtil jwtTokenUtil;

    public ClienteService(ClienteRepository clienteRepository, BCryptPasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public List<Cliente> obtenerClientes() {
        return clienteRepository.findAll();
    }

    public Cliente obtenerClientePorUsuario(String usuario) {
        return clienteRepository.findByUsuario(usuario);
    }

    public Cliente obtenerClientePorId(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "clienteNoExiste"));
    }

    public Cliente save(Cliente cliente) {
        if (clienteRepository.existsByUsuario(cliente.getUsuario())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombreUsuarioExiste");
        }
        if (cliente.getEmail() != null && clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "emailExiste");
        }
        if (cliente.getDni() != null && clienteRepository.existsByDni(cliente.getDni())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dniExiste");
        }
        if(cliente.getFechaNac() != null) {
            LocalDate fechaHoy = LocalDate.now();
            if (cliente.getFechaNac().isAfter(fechaHoy)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fechaInvalida");
            }
        }

        if (cliente.getRol() == null) {
            cliente.setRol("USER");
        }

        BCryptPasswordEncoder newPasswordEncoder = new BCryptPasswordEncoder();
        cliente.setContrasenha(newPasswordEncoder.encode(cliente.getContrasenha()));

        return clienteRepository.save(cliente);
    }

    public String login(Cliente cliente) {
        if (!clienteRepository.existsByUsuario(cliente.getUsuario())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "usuarioNoExiste");
        }
        Cliente usuario = clienteRepository.findByUsuario(cliente.getUsuario());

        if (!passwordEncoder.matches(cliente.getContrasenha(), usuario.getContrasenha())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "contrasenhaIncorrecta");
        }

        return jwtTokenUtil.generateToken(usuario.getUsuario(), usuario.getRol());
    }

    public Cliente actualizarCliente(Cliente cliente) {
        if (cliente.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "idRequerido");
        }
        
        Cliente clienteExistente = clienteRepository.findById(cliente.getId());
    
        if (!clienteExistente.getEmail().equals(cliente.getEmail()) &&
            clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "emailExiste");
        }
    
        if (!clienteExistente.getDni().equals(cliente.getDni()) &&
            clienteRepository.existsByDni(cliente.getDni())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dniExiste");
        }
    
        LocalDate fechaHoy = LocalDate.now();
        if (cliente.getFechaNac().isAfter(fechaHoy)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fechaInvalida");
        }
    
        if (cliente.getRol() == null) {
            cliente.setRol(clienteExistente.getRol());
        }
    
        if (cliente.getContrasenha() != null && !cliente.getContrasenha().equals(clienteExistente.getContrasenha())) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            cliente.setContrasenha(passwordEncoder.encode(cliente.getContrasenha()));
        }else {
            cliente.setContrasenha(clienteExistente.getContrasenha());
        }
    
        cliente.setDirecciones(clienteExistente.getDirecciones());
        cliente.setPagos(clienteExistente.getPagos());
    
        return clienteRepository.save(cliente);
    }

    public void deleteCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "clienteNoExiste");
        }
        try {
            clienteRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "clienteNoSePuedeEliminar", e);
        }
    }
}