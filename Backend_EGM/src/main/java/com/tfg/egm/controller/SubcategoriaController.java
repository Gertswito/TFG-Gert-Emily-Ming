package com.tfg.egm.controller;

import com.tfg.egm.entity.Categoria;
import com.tfg.egm.entity.Subcategoria;
import com.tfg.egm.service.SubcategoriaService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

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

    @PostMapping("/subcategorias/new")
    public ResponseEntity<Object> createSubcategoria(@RequestBody Subcategoria subcategoria) throws URISyntaxException {
        try {
            Subcategoria nuevaSubcategoria = subcategoriaService.save(subcategoria);
            URI location = new URI("/categorias/new/" + nuevaSubcategoria.getId());
            return ResponseEntity.created(location).body(nuevaSubcategoria);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("error", ex.getReason()));
        }
    }
}