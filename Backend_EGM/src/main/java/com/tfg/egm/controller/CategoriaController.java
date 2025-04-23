package com.tfg.egm.controller;

import com.tfg.egm.entity.Categoria;
import com.tfg.egm.service.CategoriaService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping("/categorias/all")
    public List<Categoria> obtenerCategorias() {
        return categoriaService.obtenerCategorias();
    }

    @GetMapping("/categorias/find/{id}")
    public ResponseEntity<Categoria> obtenerCategoriaPorId(@PathVariable Long id) {
        try {
            Categoria categoria = categoriaService.obtenerCategoriaPorId(id);
            return ResponseEntity.ok(categoria);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/categorias/delete/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        try {
            categoriaService.deleteCategoria(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/categorias/new")
    public ResponseEntity<Object> createCategoria(@RequestBody Categoria categoria) throws URISyntaxException {
        try {
            Categoria nuevaCategoria = categoriaService.save(categoria);
            URI location = new URI("/categorias/new/" + nuevaCategoria.getId());
            return ResponseEntity.created(location).body(nuevaCategoria);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("error", ex.getReason()));
        }
    }

    @PutMapping("/categorias/update/{id}")
    public ResponseEntity<Categoria> updateCategoria(@PathVariable Long id, @RequestBody Categoria categoria) {
        try {
            Categoria categoriaExistente = categoriaService.obtenerCategoriaPorId(id);
            categoria.setId(categoriaExistente.getId());
            categoria.setSubcategorias(categoriaExistente.getSubcategorias());
            Categoria categoriaActualizada = categoriaService.save(categoria);
            return ResponseEntity.ok(categoriaActualizada);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}