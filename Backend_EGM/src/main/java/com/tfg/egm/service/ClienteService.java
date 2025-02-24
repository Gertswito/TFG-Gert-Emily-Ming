package com.tfg.egm.service;

import com.tfg.egm.entity.Cliente;
import com.tfg.egm.repository.ClienteRepository;
import com.tfg.egm.security.JwtTokenUtil;
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
}