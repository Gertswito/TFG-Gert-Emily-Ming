package com.tfg.egm.service;

import com.tfg.egm.entity.Categoria;
import com.tfg.egm.entity.Subcategoria;
import com.tfg.egm.repository.SubcategoriaRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SubcategoriaService {

    private final SubcategoriaRepository subcategoriaRepository;

    public SubcategoriaService(SubcategoriaRepository subcategoriaRepository) {
        this.subcategoriaRepository = subcategoriaRepository;
    }

    public List<Subcategoria> obtenerSubcategorias() {
        return subcategoriaRepository.findAll();
    }

    public Subcategoria obtenerSubcategoriaPorId(Long id) {
        return subcategoriaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "subcategoriaNoExiste"));
    }

    public List<Subcategoria> obtenerSubcategoriasConIdCategoria(int id) {
        return subcategoriaRepository.findByCategoriaId(id);
    }

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

    public Subcategoria save(Subcategoria subcategoria) {
        if (subcategoriaRepository.existsByNombre(subcategoria.getNombre())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombreExiste");
        }
        return subcategoriaRepository.save(subcategoria);
    }
}