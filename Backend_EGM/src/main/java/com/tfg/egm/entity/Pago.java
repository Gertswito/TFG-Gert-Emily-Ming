package com.tfg.egm.entity;

import java.io.Serializable;
import java.time.LocalDate;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Entidad que representa un método de pago de un cliente.
 * Incluye los datos de la tarjeta y su relación con el cliente.
 */
@Entity
@SuppressWarnings("common-java:DuplicatedBlocks")
@Table(name = "pago")
public class Pago implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identificador único del método de pago. Se genera automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Número de la tarjeta.
     */
    @Column(name = "numero_tarjeta")
    private Long numeroTarjeta;

    /**
     * Fecha de caducidad de la tarjeta.
     */
    @Column(name = "fecha_caducidad")
    private LocalDate fechaCaducidad;

    /**
     * Código de seguridad (CVV) de la tarjeta.
     */
    @Column(name = "cvv")
    private Integer cvv;

    /**
     * Cliente al que pertenece este método de pago.
     */
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonIgnoreProperties(value = { "pagos" }, allowSetters = true)
    private Cliente cliente;

    /**
     * Indica si el método de pago está activo.
     */
    @Column(name = "activo", columnDefinition = "TINYINT(1)")
    private Boolean activo = true;

    /**
     * Devuelve el ID del método de pago.
     *
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el ID del método de pago.
     *
     * @param id nuevo id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Devuelve el número de tarjeta.
     *
     * @return número de tarjeta
     */
    public Long getNumeroTarjeta() {
        return numeroTarjeta;
    }

    /**
     * Establece el número de tarjeta.
     *
     * @param numeroTarjeta nuevo número de tarjeta
     */
    public void setNumeroTarjeta(Long numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    /**
     * Devuelve la fecha de caducidad de la tarjeta.
     *
     * @return fecha de caducidad
     */
    public LocalDate getFechaCaducidad() {
        return fechaCaducidad;
    }

    /**
     * Establece la fecha de caducidad de la tarjeta.
     *
     * @param fechaCaducidad nueva fecha de caducidad
     */
    public void setFechaCaducidad(LocalDate fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    /**
     * Devuelve el CVV de la tarjeta.
     *
     * @return cvv
     */
    public Integer getCvv() {
        return cvv;
    }

    /**
     * Establece el CVV de la tarjeta.
     *
     * @param cvv nuevo cvv
     */
    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }

    /**
     * Devuelve el cliente asociado a este método de pago.
     *
     * @return cliente
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Establece el cliente asociado a este método de pago.
     *
     * @param cliente nuevo cliente
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * Indica si el método de pago está activo.
     *
     * @return true si está activo, false si no
     */
    public Boolean getActivo() {
        return activo;
    }

    /**
     * Cambia el estado de activo del método de pago.
     *
     * @param activo nuevo estado
     */
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
