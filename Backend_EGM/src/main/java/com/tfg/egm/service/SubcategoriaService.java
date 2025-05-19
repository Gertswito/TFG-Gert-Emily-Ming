package com.tfg.egm.service;

import com.tfg.egm.entity.Subcategoria;
import com.tfg.egm.repository.SubcategoriaRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Servicio para la gestión de subcategorías.
 * Proporciona métodos para obtener, buscar, crear, actualizar y eliminar subcategorías.
 */
@Service
public class SubcategoriaService {

    private final SubcategoriaRepository subcategoriaRepository;

    /**
     * Constructor que inyecta el repositorio de subcategorías.
     * @param subcategoriaRepository repositorio de subcategorías
     */
    public SubcategoriaService(SubcategoriaRepository subcategoriaRepository) {
        this.subcategoriaRepository = subcategoriaRepository;
    }

    /**
     * Obtiene la lista de todas las subcategorías.
     * @return lista de subcategorías
     */
    public List<Subcategoria> obtenerSubcategorias() {
        return subcategoriaRepository.findAll();
    }

    /**
     * Obtiene una subcategoría por su ID.
     * @param id identificador de la subcategoría
     * @return la subcategoría encontrada
     * @throws ResponseStatusException si no existe la subcategoría
     */
    public Subcategoria obtenerSubcategoriaPorId(Long id) {
        return subcategoriaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "subcategoriaNoExiste"));
    }

    /**
     * Obtiene las subcategorías de una categoría por su ID.
     * @param id identificador de la categoría
     * @return lista de subcategorías de la categoría
     */
    public List<Subcategoria> obtenerSubcategoriasConIdCategoria(int id) {
        return subcategoriaRepository.findByCategoriaId(id);
    }

    /**
     * Busca subcategorías por nombre.
     * @param texto texto de búsqueda
     * @return lista de subcategorías encontradas
     */
    public List<Subcategoria> buscarPorNombreSubcategoria(String texto) {
        return subcategoriaRepository.subcategoriaBusquedaNombres(texto);
    }

    /**
     * Busca subcategorías por nombre y por categoría.
     * @param texto texto de búsqueda
     * @param id identificador de la categoría
     * @return lista de subcategorías encontradas
     */
    public List<Subcategoria> buscarPorNombreSubcategoriaIdCategoria(String texto, Long id) {
        return subcategoriaRepository.subcategoriaBusquedaNombres(texto, id);
    }

    /**
     * Busca subcategorías para administración filtrando por varios campos.
     * @param texto texto de búsqueda
     * @return lista de subcategorías encontradas
     */
    public List<Subcategoria> buscarSubcategoriaAdmin(String texto) {
        return subcategoriaRepository.buscarSubcategoriaAdmin(texto);
    }

    /**
     * Elimina una subcategoría por su ID.
     * @param id identificador de la subcategoría
     * @throws ResponseStatusException si no existe la subcategoría o no se puede eliminar
     */
    public void deleteSubcategoria(Long id) {
        if (!subcategoriaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "subcategoriaNoExiste");
        }
        try {
            subcategoriaRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "subcategoriaNoSePuedeEliminar", e);
        }
    }

    /**
     * Guarda una nueva subcategoría.
     * @param subcategoria objeto subcategoría a guardar
     * @return la subcategoría guardada
     * @throws ResponseStatusException si el nombre ya existe
     */
    public Subcategoria save(Subcategoria subcategoria) {
        if (subcategoriaRepository.existsByNombre(subcategoria.getNombre())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombreExiste");
        }
        return subcategoriaRepository.save(subcategoria);
    }

    /**
     * Actualiza una subcategoría existente.
     * @param subcategoria objeto subcategoría con los nuevos datos
     * @return la subcategoría actualizada
     * @throws ResponseStatusException si el nombre ya existe para otra subcategoría
     */
    public Subcategoria update(Subcategoria subcategoria) {
        Subcategoria existente = subcategoriaRepository.findByNombre(subcategoria.getNombre());
        if (existente != null && !existente.getId().equals(subcategoria.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombreExiste");
        }
        return subcategoriaRepository.save(subcategoria);
    }
}