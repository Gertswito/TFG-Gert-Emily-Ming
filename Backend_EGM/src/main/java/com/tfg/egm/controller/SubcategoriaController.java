package com.tfg.egm.controller;

import com.tfg.egm.entity.Subcategoria;
import com.tfg.egm.service.SubcategoriaService;
import org.springframework.web.bind.annotation.GetMapping;
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
}