package com.tfg.egm.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

/**
 * Entidad que representa un cliente del sistema.
 * Un cliente puede tener múltiples direcciones y métodos de pago asociados.
 */
@Entity
@SuppressWarnings("common-java:DuplicatedBlocks")
@Table(name = "cliente")
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identificador único del cliente. Se genera automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Rol del cliente (por ejemplo: "usuario", "admin").
     */
    @Column(name = "rol")
    private String rol;

    /**
     * DNI del cliente.
     */
    @Column(name = "dni")
    private String dni;

    /**
     * Nombre del cliente.
     */
    @Column(name = "nombre")
    private String nombre;

    /**
     * Apellidos del cliente.
     */
    @Column(name = "apellidos")
    private String apellidos;

    /**
     * Nombre de usuario del cliente.
     */
    @Column(name = "usuario")
    private String usuario;

    /**
     * Correo electrónico del cliente.
     */
    @Column(name = "email")
    private String email;

    /**
     * Contraseña del cliente (almacenada con hash).
     */
    @Column(name = "contrasenha")
    private String contrasenha;

    /**
     * Fecha de nacimiento del cliente.
     */
    @Column(name = "fecha_nac")
    private LocalDate fechaNac;

    /**
     * Teléfono de contacto del cliente.
     */
    @Column(name = "telefono")
    private String telefono;

    /**
     * Conjunto de direcciones asociadas al cliente.
     */
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = { "cliente" }, allowSetters = true)
    private Set<Direccion> direcciones = new HashSet<>();

    /**
     * Conjunto de métodos de pago asociados al cliente.
     */
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = { "cliente" }, allowSetters = true) 
    private Set<Pago> pagos = new HashSet<>();

    /**
     * Obtiene el identificador del cliente.
     *
     * @return ID del cliente
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el identificador del cliente.
     *
     * @param id nuevo ID del cliente
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene el rol del cliente.
     *
     * @return rol del cliente
     */
    public String getRol() {
        return rol;
    }

    /**
     * Establece el rol del cliente.
     *
     * @param rol nuevo rol del cliente
     */
    public void setRol(String rol) {
        this.rol = rol;
    }

    /**
     * Obtiene el DNI del cliente.
     *
     * @return DNI del cliente
     */
    public String getDni() {
        return dni;
    }

    /**
     * Establece el DNI del cliente.
     *
     * @param dni nuevo DNI
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Obtiene el nombre del cliente.
     *
     * @return nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del cliente.
     *
     * @param nombre nuevo nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene los apellidos del cliente.
     *
     * @return apellidos
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Establece los apellidos del cliente.
     *
     * @param apellidos nuevos apellidos
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Obtiene el nombre de usuario.
     *
     * @return nombre de usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Establece el nombre de usuario.
     *
     * @param usuario nuevo nombre de usuario
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene el correo electrónico del cliente.
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del cliente.
     *
     * @param email nuevo email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene la contraseña del cliente (hash).
     *
     * @return contraseña
     */
    public String getContrasenha() {
        return contrasenha;
    }

    /**
     * Establece la contraseña del cliente (hash).
     *
     * @param contrasenha nueva contraseña
     */
    public void setContrasenha(String contrasenha) {
        this.contrasenha = contrasenha;
    }

    /**
     * Obtiene la fecha de nacimiento del cliente.
     *
     * @return fecha de nacimiento
     */
    public LocalDate getFechaNac() {
        return fechaNac;
    }

    /**
     * Establece la fecha de nacimiento del cliente.
     *
     * @param fechaNac nueva fecha de nacimiento
     */
    public void setFechaNac(LocalDate fechaNac) {
        this.fechaNac = fechaNac;
    }

    /**
     * Obtiene el teléfono del cliente.
     *
     * @return número de teléfono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el teléfono del cliente.
     *
     * @param telefono nuevo teléfono
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Devuelve las direcciones asociadas al cliente.
     *
     * @return conjunto de direcciones
     */
    public Set<Direccion> getDirecciones() {
        return direcciones;
    }

    /**
     * Establece el conjunto de direcciones del cliente.
     *
     * @param direcciones nuevo conjunto de direcciones
     */
    public void setDirecciones(Set<Direccion> direcciones) {
        this.direcciones = direcciones;
    }

    /**
     * Devuelve los métodos de pago asociados al cliente.
     *
     * @return conjunto de pagos
     */
    public Set<Pago> getPagos() {
        return pagos;
    }

    /**
     * Establece los métodos de pago asociados al cliente.
     *
     * @param pagos nuevo conjunto de pagos
     */
    public void setPagos(Set<Pago> pagos) {
        this.pagos = pagos;
    }
}
