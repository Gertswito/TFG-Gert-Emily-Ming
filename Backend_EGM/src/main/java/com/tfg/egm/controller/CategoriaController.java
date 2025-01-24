package com.tfg.egm.controller;

import com.tfg.egm.entity.Categoria;
import com.tfg.egm.service.CategoriaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}