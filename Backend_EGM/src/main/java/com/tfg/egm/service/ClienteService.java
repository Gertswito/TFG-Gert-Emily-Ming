package com.tfg.egm.service;

import com.tfg.egm.entity.Cliente;
import com.tfg.egm.repository.ClienteRepository;
import com.tfg.egm.security.JwtTokenUtil;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para la gestión de clientes.
 * Proporciona métodos para obtener, buscar, crear, actualizar, eliminar clientes y login.
 */
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtTokenUtil jwtTokenUtil;

    /**
     * Constructor que inyecta el repositorio de clientes, el codificador de contraseñas y el utilitario JWT.
     * @param clienteRepository repositorio de clientes
     * @param passwordEncoder codificador de contraseñas
     * @param jwtTokenUtil utilitario para JWT
     */
    public ClienteService(ClienteRepository clienteRepository, BCryptPasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * Obtiene la lista de todos los clientes.
     * @return lista de clientes
     */
    public List<Cliente> obtenerClientes() {
        return clienteRepository.findAll();
    }

    /**
     * Obtiene un cliente por su usuario.
     * @param usuario nombre de usuario
     * @return cliente encontrado
     */
    public Cliente obtenerClientePorUsuario(String usuario) {
        return clienteRepository.findByUsuario(usuario);
    }

    /**
     * Obtiene un cliente por su ID.
     * @param id identificador del cliente
     * @return cliente encontrado
     * @throws ResponseStatusException si no existe el cliente
     */
    public Cliente obtenerClientePorId(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "clienteNoExiste"));
    }

    /**
     * Busca clientes para administración filtrando por varios campos.
     * @param texto texto de búsqueda
     * @return lista de clientes encontrados
     */
    public List<Cliente> buscarClienteAdmin(String texto) {
        return clienteRepository.buscarClienteAdmin(texto);
    }

    /**
     * Guarda un nuevo cliente, validando usuario, email, dni y fecha de nacimiento.
     * @param cliente objeto cliente a guardar
     * @return el cliente guardado
     * @throws ResponseStatusException si ya existe usuario, email, dni o la fecha es inválida
     */
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

    /**
     * Realiza el login de un cliente y genera un token JWT.
     * @param cliente objeto cliente con usuario y contraseña
     * @return token JWT generado
     * @throws ResponseStatusException si el usuario no existe o la contraseña es incorrecta
     */
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

    /**
     * Actualiza un cliente existente, validando email, dni, fecha y contraseña.
     * @param cliente objeto cliente con los nuevos datos
     * @return cliente actualizado
     * @throws ResponseStatusException si hay datos inválidos o duplicados
     */
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

    /**
     * Elimina un cliente por su ID.
     * @param id identificador del cliente
     * @throws ResponseStatusException si no existe el cliente o no se puede eliminar
     */
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