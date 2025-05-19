package com.tfg.egm.entity;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

/**
 * Entidad que representa una línea de venta dentro de una venta.
 * Cada línea de venta está asociada a una venta y a un producto, e indica la cantidad pedida,
 * el precio unitario y el precio total de ese producto en la venta.
 */
@Entity
@SuppressWarnings("common-java:DuplicatedBlocks")
@Table(name = "lineasventa")
public class LineasVentas implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único de la línea de venta. Se genera automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Venta a la que pertenece la línea de venta.
     */
    @ManyToOne
    @JoinColumn(name = "venta_id")
    @JsonIgnoreProperties(value = { "lineasVentas", "cliente", "direccion", "pago" }, allowSetters = true)
    private Venta venta;

    /**
     * Producto asociado a la línea de venta.
     */
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    /**
     * Cantidad pedida de este producto en la venta.
     */
    @Column(name = "cantidad_pedida")
    private Long cantidadPedida;

    /**
     * Precio unitario del producto en el momento de la venta.
     */
    @Column(name = "precio_unitario")
    private Float precioUnitario;

    /**
     * Precio total de la línea de venta (cantidad * precio unitario).
     */
    @Column(name = "precio_total")
    private Float precioTotal;

    /**
     * Obtiene el identificador de la línea de venta.
     *
     * @return ID de la línea de venta
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el identificador de la línea de venta.
     *
     * @param id nuevo ID de la línea de venta
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene la venta asociada a la línea de venta.
     *
     * @return venta
     */
    public Venta getVenta() {
        return venta;
    }

    /**
     * Establece la venta asociada a la línea de venta.
     *
     * @param venta nueva venta
     */
    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    /**
     * Obtiene el producto asociado a esta línea de venta.
     *
     * @return producto
     */
    public Producto getProducto() {
        return producto;
    }

    /**
     * Establece el producto asociado a la línea de venta.
     *
     * @param producto nuevo producto
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    /**
     * Obtiene la cantidad pedida del producto.
     *
     * @return cantidad pedida
     */
    public Long getCantidadPedida() {
        return cantidadPedida;
    }

    /**
     * Establece la cantidad pedida del producto.
     *
     * @param cantidadPedida nueva cantidad pedida
     */
    public void setCantidadPedida(Long cantidadPedida) {
        this.cantidadPedida = cantidadPedida;
    }

    /**
     * Obtiene el precio unitario del producto.
     *
     * @return precio unitario
     */
    public Float getPrecioUnitario() {
        return precioUnitario;
    }

    /**
     * Establece el precio unitario del producto.
     *
     * @param precioUnitario nuevo precio unitario
     */
    public void setPrecioUnitario(Float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    /**
     * Obtiene el precio total de la línea de venta.
     *
     * @return precio total
     */
    public Float getPrecioTotal() {
        return precioTotal;
    }

    /**
     * Establece el precio total de la línea de venta.
     *
     * @param precioTotal nuevo precio total
     */
    public void setPrecioTotal(Float precioTotal) {
        this.precioTotal = precioTotal;
    }
}
