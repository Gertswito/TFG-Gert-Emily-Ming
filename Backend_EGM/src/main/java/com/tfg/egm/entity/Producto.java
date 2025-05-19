package com.tfg.egm.entity;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

/**
 * Entidad que representa un producto.
 * Incluye información básica, categoría, subcategoría y detalles como ingredientes, stock y precio.
 */
@Entity
@SuppressWarnings("common-java:DuplicatedBlocks")
@Table(name = "producto")
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 
     * ID único del producto. 
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 
     * Referencia interna del producto. 
     */
    @Column(name = "referencia")
    private String referencia;

    /** 
     * URL de la imagen del producto. 
     */
    @Column(name = "url_imagen", length = 3000)
    private String urlImagen;

    /** 
     * Nombre del producto. 
     */
    @Column(name = "nombre")
    private String nombre;

    /** 
     * Marca del producto. 
     */
    @Column(name = "marca")
    private String marca;

    /** 
     * Categoría principal del producto. 
     */
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    @JsonIgnoreProperties(value = { "subcategorias" }, allowSetters = true)
    private Categoria categoria;

    /** 
     * Subcategoría del producto. 
     */
    @ManyToOne
    @JoinColumn(name = "subcategoria_id")
    @JsonIgnoreProperties(value = { "categoria" }, allowSetters = true)
    private Subcategoria subcategoria;

    /** 
     * Descripción del producto. 
     */
    @Column(name = "descripcion", length = 3000)
    private String descripcion;

    /** 
     * Ingredientes del producto. 
     */
    @Column(name = "ingredientes", length = 3000)
    private String ingredientes;

    /** 
     * Tipo de IVA aplicado. 
     */
    @Column(name = "tipo_IVA")
    private Float tipoIVA;

    /** 
     * Cantidad por unidad (por ejemplo, gramos o mililitros). 
     */
    @Column(name = "cantidad")
    private Long cantidad;

    /** 
     * Stock disponible. 
     */
    @Column(name = "stock")
    private Long stock;

    /** 
     * Precio del producto. 
     */
    @Column(name = "precio")
    private Float precio;

    /**
     * Devuelve el ID del producto.
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el ID del producto.
     * @param id nuevo id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Devuelve la referencia del producto.
     * @return referencia
     */
    public String getReferencia() {
        return referencia;
    }

    /**
     * Establece la referencia del producto.
     * @param referencia nueva referencia
     */
    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    /**
     * Devuelve la URL de la imagen.
     * @return urlImagen
     */
    public String getUrlImagen() {
        return urlImagen;
    }

    /**
     * Establece la URL de la imagen.
     * @param urlImagen nueva URL
     */
    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    /**
     * Devuelve el nombre del producto.
     * @return nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del producto.
     * @param nombre nuevo nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve la marca del producto.
     * @return marca
     */
    public String getMarca() {
        return marca;
    }

    /**
     * Establece la marca del producto.
     * @param marca nueva marca
     */
    public void setMarca(String marca) {
        this.marca = marca;
    }

    /**
     * Devuelve la categoría del producto.
     * @return categoria
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * Establece la categoría del producto.
     * @param categoria nueva categoría
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /**
     * Devuelve la subcategoría del producto.
     * @return subcategoria
     */
    public Subcategoria getSubcategoria() {
        return subcategoria;
    }

    /**
     * Establece la subcategoría del producto.
     * @param subcategoria nueva subcategoría
     */
    public void setSubcategoria(Subcategoria subcategoria) {
        this.subcategoria = subcategoria;
    }

    /**
     * Devuelve la descripción del producto.
     * @return descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción del producto.
     * @param descripcion nueva descripción
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Devuelve los ingredientes del producto.
     * @return ingredientes
     */
    public String getIngredientes() {
        return ingredientes;
    }

    /**
     * Establece los ingredientes del producto.
     * @param ingredientes nuevos ingredientes
     */
    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    /**
     * Devuelve el tipo de IVA.
     * @return tipoIVA
     */
    public Float getTipoIVA() {
        return tipoIVA;
    }

    /**
     * Establece el tipo de IVA.
     * @param tipoIVA nuevo tipo de IVA
     */
    public void setTipoIVA(Float tipoIVA) {
        this.tipoIVA = tipoIVA;
    }

    /**
     * Devuelve la cantidad por unidad.
     * @return cantidad
     */
    public Long getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad por unidad.
     * @param cantidad nueva cantidad
     */
    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Devuelve el stock disponible.
     * @return stock
     */
    public Long getStock() {
        return stock;
    }

    /**
     * Establece el stock disponible.
     * @param stock nuevo stock
     */
    public void setStock(Long stock) {
        this.stock = stock;
    }

    /**
     * Devuelve el precio del producto.
     * @return precio
     */
    public Float getPrecio() {
        return precio;
    }

    /**
     * Establece el precio del producto.
     * @param precio nuevo precio
     */
    public void setPrecio(Float precio) {
        this.precio = precio;
    }
}
