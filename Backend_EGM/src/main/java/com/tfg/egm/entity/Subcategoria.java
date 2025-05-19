package com.tfg.egm.entity;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

/**
 * Entidad que representa una subcategoría.
 * Cada subcategoría pertenece a una categoría principal.
 */
@Entity
@SuppressWarnings("common-java:DuplicatedBlocks")
@Table(name = "subcategoria")
public class Subcategoria implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identificador único de la subcategoría. Se genera automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nombre de la subcategoría.
     */
    @Column(name = "nombre")
    private String nombre;

    /**
     * URL de la imagen de la subcategoría.
     */
    @Column(name = "imagen_subcategoria", length = 3000)
    private String imagenSubcategoria;

    /**
     * Categoría principal a la que pertenece esta subcategoría.
     */
    @ManyToOne
    @JoinColumn(name = "categoria_id")    
    @JsonIgnoreProperties(value = { "subcategorias" }, allowSetters = true)
    private Categoria categoria;

    /**
     * Devuelve el ID de la subcategoría.
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el ID de la subcategoría.
     * @param id nuevo id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre de la subcategoría.
     * @return nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la subcategoría.
     * @param nombre nuevo nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve la URL de la imagen de la subcategoría.
     * @return imagenSubcategoria
     */
    public String getImagenSubcategoria() {
        return imagenSubcategoria;
    }

    /**
     * Establece la URL de la imagen de la subcategoría.
     * @param imagenSubcategoria nueva URL
     */
    public void setImagenSubcategoria(String imagenSubcategoria) {
        this.imagenSubcategoria = imagenSubcategoria;
    }

    /**
     * Devuelve la categoría principal de la subcategoría.
     * @return categoria
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * Establece la categoría principal de la subcategoría.
     * @param categoria nueva categoría
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}