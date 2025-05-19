package com.tfg.egm.controller;

import com.tfg.egm.entity.Subcategoria;
import com.tfg.egm.service.SubcategoriaService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar las subcategorías.
 * Permite obtener, buscar, crear, actualizar y eliminar subcategorías.
 */
@RestController
public class SubcategoriaController {

    private final SubcategoriaService subcategoriaService;

    /**
     * Constructor que inyecta el servicio de subcategorías.
     * @param subcategoriaService servicio de subcategorías
     */
    public SubcategoriaController(SubcategoriaService subcategoriaService) {
        this.subcategoriaService = subcategoriaService;
    }

    /**
     * Obtiene la lista de todas las subcategorías.
     * @return lista de subcategorías
     */
    @GetMapping("/subcategorias/all")
    public List<Subcategoria> obtenerSubcategorias() {
        return subcategoriaService.obtenerSubcategorias();
    }

    /**
     * Obtiene una subcategoría por su ID.
     * @param id identificador de la subcategoría
     * @return ResponseEntity con la subcategoría o 404 si no se encuentra
     */
    @GetMapping("/subcategorias/find/{id}")
    public ResponseEntity<Subcategoria> obtenerSubcategoriaPorId(@PathVariable Long id) {
        try {
            Subcategoria subcategoria = subcategoriaService.obtenerSubcategoriaPorId(id);
            return ResponseEntity.ok(subcategoria);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene las subcategorías de una categoría por su ID.
     * @param id identificador de la categoría
     * @return lista de subcategorías de la categoría
     */
    @GetMapping("/subcategorias/categoria/{id}")
    public List<Subcategoria> obtenerSubcategoriasConIdCategoria(@PathVariable int id) {
        return subcategoriaService.obtenerSubcategoriasConIdCategoria(id);
    }

    /**
     * Busca subcategorías por nombre.
     * @param texto texto de búsqueda
     * @return ResponseEntity con la lista de subcategorías encontradas o 404
     */
    @GetMapping("/subcategorias/busqueda/{texto}")
    public ResponseEntity<List<Subcategoria>> obtenerSubcategoriaFiltro(@PathVariable String texto) {
        try {
            List<Subcategoria> subcategorias = subcategoriaService.buscarPorNombreSubcategoria(texto);
            return ResponseEntity.ok(subcategorias);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Busca subcategorías por nombre y categoría.
     * @param texto texto de búsqueda
     * @param id identificador de la categoría
     * @return ResponseEntity con la lista de subcategorías encontradas o 404
     */
    @GetMapping("/subcategorias/busqueda/{texto}/categoria/{id}")
    public ResponseEntity<List<Subcategoria>> obtenerSubcategoriaFiltroCategoria(@PathVariable String texto, @PathVariable Long id) {
        try {
            List<Subcategoria> subcategorias = subcategoriaService.buscarPorNombreSubcategoriaIdCategoria(texto, id);
            return ResponseEntity.ok(subcategorias);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Busca subcategorías para administración.
     * @param texto texto de búsqueda
     * @return ResponseEntity con la lista de subcategorías encontradas o 404
     */
    @GetMapping("/subcategorias/admin-busqueda/{texto}")
    public ResponseEntity<List<Subcategoria>> obtenerSubcategoriaFiltroAdmin(@PathVariable String texto) {
        try {
            List<Subcategoria> subcategorias = subcategoriaService.buscarSubcategoriaAdmin(texto);
            return ResponseEntity.ok(subcategorias);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Elimina una subcategoría por su ID.
     * @param id identificador de la subcategoría
     * @return ResponseEntity sin contenido o 404 si no se encuentra
     */
    @DeleteMapping("/subcategorias/delete/{id}")
    public ResponseEntity<Void> deleteSubcategoria(@PathVariable Long id) {
        try {
            subcategoriaService.deleteSubcategoria(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crea una nueva subcategoría.
     * @param subcategoria objeto subcategoría a crear
     * @return ResponseEntity con la nueva subcategoría y la ubicación
     * @throws URISyntaxException si la URI no es válida
     */
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

    /**
     * Actualiza una subcategoría existente.
     * @param id identificador de la subcategoría
     * @param subcategoria objeto subcategoría con los nuevos datos
     * @return ResponseEntity con la subcategoría actualizada o 404 si no se encuentra
     */
    @PutMapping("/subcategorias/update/{id}")
    public ResponseEntity<Subcategoria> updateSubcategoria(@PathVariable Long id, @RequestBody Subcategoria subcategoria) {
        try {
            if (subcategoriaService.obtenerSubcategoriaPorId(id) != null) {
                subcategoriaService.update(subcategoria);
                return ResponseEntity.ok(subcategoria);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (ResponseStatusException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}