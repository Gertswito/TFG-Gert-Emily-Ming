package com.tfg.egm.entity;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

/**
 * Entidad que representa una dirección asociada a un cliente.
 * Cada dirección contiene información sobre la ubicación y está vinculada a un cliente.
 */
@Entity
@SuppressWarnings("common-java:DuplicatedBlocks")
@Table(name = "direccion")
public class Direccion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identificador único de la dirección. Se genera automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Texto descriptivo de la dirección.
     */
    @Column(name = "direccion")
    private String direccion;

    /**
     * Código postal de la dirección.
     */
    @Column(name = "codigo_postal")
    private Long codigoPostal;

    /**
     * Localidad de la dirección.
     */
    @Column(name = "localidad")
    private String localidad;

    /**
     * Comunidad autónoma de la dirección.
     */
    @Column(name = "comunidad_autonoma")
    private String comunidadAutonoma;

    /**
     * Cliente al que pertenece esta dirección.
     */
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonIgnoreProperties(value = { "direcciones" }, allowSetters = true)
    private Cliente cliente;

    /**
     * Indica si la dirección está activa.
     */
    @Column(name= "activo", columnDefinition = "TINYINT(1)")
    private Boolean activo = true;

    /**
     * Obtiene el identificador de la dirección.
     *
     * @return ID de la dirección
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el identificador de la dirección.
     *
     * @param id nuevo ID de la dirección
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene el texto descriptivo de la dirección.
     *
     * @return dirección
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Establece el texto descriptivo de la dirección.
     *
     * @param direccion nueva dirección
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Obtiene el código postal de la dirección.
     *
     * @return código postal
     */
    public Long getCodigoPostal() {
        return codigoPostal;
    }

    /**
     * Establece el código postal de la dirección.
     *
     * @param codigoPostal nuevo código postal
     */
    public void setCodigoPostal(Long codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    /**
     * Obtiene la localidad de la dirección.
     *
     * @return localidad
     */
    public String getLocalidad() {
        return localidad;
    }

    /**
     * Establece la localidad de la dirección.
     *
     * @param localidad nueva localidad
     */
    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    /**
     * Obtiene la comunidad autónoma de la dirección.
     *
     * @return comunidad autónoma
     */
    public String getComunidadAutonoma() {
        return comunidadAutonoma;
    }

    /**
     * Establece la comunidad autónoma de la dirección.
     *
     * @param comunidadAutonoma nueva comunidad autónoma
     */
    public void setComunidadAutonoma(String comunidadAutonoma) {
        this.comunidadAutonoma = comunidadAutonoma;
    }

    /**
     * Obtiene el cliente asociado a esta dirección.
     *
     * @return cliente
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Establece el cliente asociado a esta dirección.
     *
     * @param cliente nuevo cliente
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * Indica si la dirección está activa.
     *
     * @return true si está activa, false en caso contrario
     */
    public Boolean getActivo() {
        return activo;
    }

    /**
     * Establece el estado de la dirección (activa o no).
     *
     * @param activo nuevo estado
     */
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}