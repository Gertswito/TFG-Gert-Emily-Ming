package com.tfg.egm.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

/**
 * Entidad que representa una venta realizada por un cliente.
 * Incluye información sobre el cliente, la fecha, el precio final, la dirección, el pago y las líneas de venta.
 */
@Entity
@SuppressWarnings("common-java:DuplicatedBlocks")
@Table(name = "venta")
public class Venta implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 
     * ID único de la venta. 
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 
     * Cliente que realiza la venta. 
     */
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    /** 
     * Fecha y hora de la venta. 
     */
    @Column(name = "fecha_hora")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaHora;

    /** Precio final de la venta. */
    @Column(name = "precio_final")
    private Float precioFinal;

    /** 
     * Dirección de entrega asociada a la venta. 
     */
    @ManyToOne
    @JoinColumn(name = "direccion_id")
    @JsonIgnoreProperties(value = { "cliente" }, allowSetters = true)
    private Direccion direccion;

    /** 
     * Método de pago utilizado en la venta. 
     */
    @ManyToOne
    @JoinColumn(name = "pago_id")
    @JsonIgnoreProperties(value = { "cliente" }, allowSetters = true)
    private Pago pago;

    /** 
     * Líneas de venta asociadas (solo para hacer la venta, no para la BBDD). 
     */
    @Transient
    private List<LineasVentas> lineasVentas;
    
    /**
     * Devuelve el ID de la venta.
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el ID de la venta.
     * @param id nuevo id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Devuelve el cliente que realiza la venta.
     * @return cliente
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Establece el cliente que realiza la venta.
     * @param cliente nuevo cliente
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * Devuelve la fecha y hora de la venta.
     * @return fechaHora
     */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /**
     * Establece la fecha y hora de la venta.
     * @param fechaHora nueva fecha y hora
     */
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    /**
     * Devuelve el precio final de la venta.
     * @return precioFinal
     */
    public Float getPrecioFinal() {
        return precioFinal;
    }

    /**
     * Establece el precio final de la venta.
     * @param precioFinal nuevo precio final
     */
    public void setPrecioFinal(Float precioFinal) {
        this.precioFinal = precioFinal;
    }

    /**
     * Devuelve la dirección de entrega.
     * @return direccion
     */
    public Direccion getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección de entrega.
     * @param direccion nueva dirección
     */
    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    /**
     * Devuelve el método de pago utilizado.
     * @return pago
     */
    public Pago getPago() {
        return pago;
    }

    /**
     * Establece el método de pago utilizado.
     * @param pago nuevo pago
     */
    public void setPago(Pago pago) {
        this.pago = pago;
    }

    /**
     * Devuelve las líneas de venta asociadas.
     * @return lista de líneas de venta
     */
    public List<LineasVentas> getLineasVentas() {
        return lineasVentas;
    }
    
    /**
     * Establece las líneas de venta asociadas.
     * @param lineasVentas nueva lista de líneas de venta
     */
    public void setLineasVentas(List<LineasVentas> lineasVentas) {
        this.lineasVentas = lineasVentas;
    }
}
