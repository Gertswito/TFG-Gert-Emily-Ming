package com.tfg.egm.service;

import com.tfg.egm.entity.Subcategoria;
import com.tfg.egm.repository.SubcategoriaRepository;
import org.springframework.stereotype.Service;

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
}