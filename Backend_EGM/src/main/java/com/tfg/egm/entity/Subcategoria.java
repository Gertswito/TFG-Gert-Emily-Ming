package com.tfg.egm.entity;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@SuppressWarnings("common-java:DuplicatedBlocks")
@Table(name = "subcategoria")
public class Subcategoria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "imagen_subcategoria", length = 3000)
    private String imagenSubcategoria;

    @ManyToOne
    @JoinColumn(name = "categoria_id")    
    @JsonIgnoreProperties(value = { "subcategorias" }, allowSetters = true)
    private Categoria categoria;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagenSubcategoria() {
        return imagenSubcategoria;
    }

    public void setImagenSubcategoria(String imagenSubcategoria) {
        this.imagenSubcategoria = imagenSubcategoria;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}