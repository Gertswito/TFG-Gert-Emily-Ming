package com.tfg.egm.controller;

import com.tfg.egm.entity.Subcategoria;
import com.tfg.egm.service.SubcategoriaService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SubcategoriaController {

    private final SubcategoriaService subcategoriaService;

    public SubcategoriaController(SubcategoriaService subcategoriaService) {
        this.subcategoriaService = subcategoriaService;
    }

    @GetMapping("/subcategorias/all")
    public List<Subcategoria> obtenerSubcategorias() {
        return subcategoriaService.obtenerSubcategorias();
    }

    @GetMapping("/subcategorias/categoria/{id}")
    public List<Subcategoria> obtenerSubcategoriasConIdCategoria(@PathVariable int id) {
        return subcategoriaService.obtenerSubcategoriasConIdCategoria(id);
    }

    @DeleteMapping("/subcategorias/delete/{id}")
    public ResponseEntity<Void> deleteSubcategoria(@PathVariable Long id) {
        try {
            subcategoriaService.deleteSubcategoria(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}