package com.tfg.egm.entity;

import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

/**
 * Entidad que representa una categoría principal dentro del sistema.
 * Cada categoría puede contener una o varias subcategorías.
 */
@Entity
@SuppressWarnings("common-java:DuplicatedBlocks")
@Table(name = "categoria")
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identificador único de la categoría. Se genera automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nombre de la categoría.
     */
    @Column(name = "nombre")
    private String nombre;

    /**
     * URL de la imagen de la categoría.
     */
    @Column(name = "imagen_categoria", length = 3000)
    private String imagenCategoria;

    /**
     * Lista de subcategorías asociadas a esta categoría.
     * Las operaciones en cascada se aplican sobre estas subcategorías.
     */
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonIgnoreProperties(value = { "categoria" }, allowSetters = true)
    private List<Subcategoria> subcategorias;

    /**
     * Obtiene el ID de la categoría.
     *
     * @return el identificador de la categoría
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el ID de la categoría.
     *
     * @param id el nuevo identificador
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de la categoría.
     *
     * @return el nombre de la categoría
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la categoría.
     *
     * @param nombre el nuevo nombre de la categoría
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la URL de la imagen de la categoría.
     *
     * @return la URL de la imagen de la categoría
     */
    public String getImagenCategoria() {
        return imagenCategoria;
    }

    /**
     * Establece la URL de la imagen de la categoría.
     *
     * @param imagenCategoria la nueva URL de la imagen de la categoría
     */
    public void setImagenCategoria(String imagenCategoria) {
        this.imagenCategoria = imagenCategoria;
    }

    /**
     * Devuelve la lista de subcategorías asociadas a esta categoría.
     *
     * @return lista de subcategorías
     */
    public List<Subcategoria> getSubcategorias() {
        return subcategorias;
    }

    /**
     * Establece la lista de subcategorías asociadas a esta categoría.
     *
     * @param subcategorias lista de subcategorías
     */
    public void setSubcategorias(List<Subcategoria> subcategorias) {
        this.subcategorias = subcategorias;
    }
}
