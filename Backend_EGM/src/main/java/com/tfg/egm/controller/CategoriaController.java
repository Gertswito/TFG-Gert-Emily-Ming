package com.tfg.egm.controller;

import com.tfg.egm.entity.Categoria;
import com.tfg.egm.service.CategoriaService;

import jakarta.persistence.EntityNotFoundException;

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


/**
 * Controlador REST para gestionar las categorías.
 * Permite obtener, buscar, crear, actualizar y eliminar categorías.
 */
@RestController
public class CategoriaController {

    private final CategoriaService categoriaService;

    /**
     * Constructor que inyecta el servicio de categorías.
     * @param categoriaService servicio de categorías
     */
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    /**
     * Obtiene la lista de todas las categorías.
     * @return lista de categorías
     */
    @GetMapping("/categorias/all")
    public List<Categoria> obtenerCategorias() {
        return categoriaService.obtenerCategorias();
    }

    /**
     * Obtiene una categoría por su ID.
     * @param id identificador de la categoría
     * @return ResponseEntity con la categoría o 404 si no se encuentra
     */
    @GetMapping("/categorias/find/{id}")
    public ResponseEntity<Categoria> obtenerCategoriaPorId(@PathVariable Long id) {
        try {
            Categoria categoria = categoriaService.obtenerCategoriaPorId(id);
            return ResponseEntity.ok(categoria);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Busca categorías por nombre o subcategoría.
     * @param texto texto de búsqueda
     * @return ResponseEntity con la lista de categorías encontradas o 404
     */
    @GetMapping("/categorias/busqueda/{texto}")
    public ResponseEntity<List<Categoria>> obtenerCategoriaFiltro(@PathVariable String texto) {
        try {
            List<Categoria> categorias = categoriaService.buscarPorNombreCategoriaOSubcategoria(texto);
            return ResponseEntity.ok(categorias);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Busca categorías para administración.
     * @param texto texto de búsqueda
     * @return ResponseEntity con la lista de categorías encontradas o 404
     */
    @GetMapping("/categorias/admin-busqueda/{texto}")
    public ResponseEntity<List<Categoria>> obtenerCategoriaFiltroAdmin(@PathVariable String texto) {
        try {
            List<Categoria> categorias = categoriaService.buscarCategoriaAdmin(texto);
            return ResponseEntity.ok(categorias);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Elimina una categoría por su ID.
     * @param id identificador de la categoría
     * @return ResponseEntity sin contenido o 404 si no se encuentra
     */
    @DeleteMapping("/categorias/delete/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        try {
            categoriaService.deleteCategoria(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crea una nueva categoría.
     * @param categoria objeto categoría a crear
     * @return ResponseEntity con la nueva categoría y la ubicación
     * @throws URISyntaxException si la URI no es válida
     */
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

    /**
     * Actualiza una categoría existente.
     * @param id identificador de la categoría
     * @param categoria objeto categoría con los nuevos datos
     * @return ResponseEntity con la categoría actualizada o 404 si no se encuentra
     */
    @PutMapping("/categorias/update/{id}")
    public ResponseEntity<Categoria> updateCategoria(@PathVariable Long id, @RequestBody Categoria categoria) {
        try {
            if (categoriaService.obtenerCategoriaPorId(id) != null) {
                categoriaService.update(categoria);
                return ResponseEntity.ok(categoria);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}