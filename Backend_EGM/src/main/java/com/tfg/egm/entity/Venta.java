package com.tfg.egm.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@SuppressWarnings("common-java:DuplicatedBlocks")
@Table(name = "venta")
public class Venta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(name = "fecha_hora")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss") // Formato ISO
    private LocalDateTime fechaHora;

    @Column(name = "precio_final")
    private Float precioFinal;

    @ManyToOne
    @JoinColumn(name = "direccion_id")
    @JsonIgnoreProperties(value = { "cliente" }, allowSetters = true)
    private Direccion direccion;

    @ManyToOne
    @JoinColumn(name = "pago_id")
    @JsonIgnoreProperties(value = { "cliente" }, allowSetters = true)
    private Pago pago;

    @Transient
    private List<LineasVentas> lineasVentas;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Float getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(Float precioFinal) {
        this.precioFinal = precioFinal;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public List<LineasVentas> getLineasVentas() {
        return lineasVentas;
    }
    
    public void setLineasVentas(List<LineasVentas> lineasVentas) {
        this.lineasVentas = lineasVentas;
    }
}
