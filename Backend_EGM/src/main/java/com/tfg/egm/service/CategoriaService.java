package com.tfg.egm.service;

import com.tfg.egm.entity.Categoria;
import com.tfg.egm.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> obtenerCategorias() {
        return categoriaRepository.findAll();
    }
}