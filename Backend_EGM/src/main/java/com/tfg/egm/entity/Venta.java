package com.tfg.egm.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

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

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "hora")
    private LocalTime hora;

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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    
    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
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
}
