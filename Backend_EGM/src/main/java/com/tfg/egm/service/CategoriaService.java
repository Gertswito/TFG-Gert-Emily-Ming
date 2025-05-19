package com.tfg.egm.service;

import com.tfg.egm.entity.Categoria;
import com.tfg.egm.repository.CategoriaRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Servicio para la gestión de categorías.
 * Proporciona métodos para obtener, buscar, crear, actualizar y eliminar categorías.
 */
@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    /**
     * Constructor que inyecta el repositorio de categorías.
     * @param categoriaRepository repositorio de categorías
     */
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * Obtiene la lista de todas las categorías.
     * @return lista de categorías
     */
    public List<Categoria> obtenerCategorias() {
        return categoriaRepository.findAll();
    }

    /**
     * Obtiene una categoría por su ID.
     * @param id identificador de la categoría
     * @return la categoría encontrada
     * @throws ResponseStatusException si no existe la categoría
     */
    public Categoria obtenerCategoriaPorId(Long id) {
        return categoriaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "categoriaNoExiste"));
    }

    /**
     * Busca categorías cuyo nombre o el de alguna subcategoría contenga el texto dado.
     * @param texto texto de búsqueda
     * @return lista de categorías encontradas
     */
    public List<Categoria> buscarPorNombreCategoriaOSubcategoria(String texto) {
        return categoriaRepository.categoriaBusquedaNombres(texto);
    }

    /**
     * Busca categorías para administración filtrando por varios campos.
     * @param texto texto de búsqueda
     * @return lista de categorías encontradas
     */
    public List<Categoria> buscarCategoriaAdmin(String texto) {
        return categoriaRepository.buscarCategoriaAdmin(texto);
    }

    /**
     * Elimina una categoría por su ID.
     * @param id identificador de la categoría
     * @throws ResponseStatusException si no existe la categoría o no se puede eliminar
     */
    public void deleteCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "categoriaNoExiste");
        }
        try {
            categoriaRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "categoriaNoSePuedeEliminar", e);
        }
    }

    /**
     * Guarda una nueva categoría.
     * @param categoria objeto categoría a guardar
     * @return la categoría guardada
     * @throws ResponseStatusException si el nombre ya existe
     */
    public Categoria save(Categoria categoria) {
        if (categoriaRepository.existsByNombre(categoria.getNombre())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombreExiste");
        }
        return categoriaRepository.save(categoria);
    }

    /**
     * Actualiza una categoría existente.
     * @param categoria objeto categoría con los nuevos datos
     * @return la categoría actualizada
     * @throws ResponseStatusException si el nombre ya existe para otra categoría
     */
    public Categoria update(Categoria categoria) {
        Categoria existente = categoriaRepository.findByNombre(categoria.getNombre());
        if (existente != null && !existente.getId().equals(categoria.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombreExiste");
        }
        return categoriaRepository.save(categoria);
    }
}