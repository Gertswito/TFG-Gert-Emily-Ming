package com.tfg.egm.controller;

import com.tfg.egm.entity.Producto;
import com.tfg.egm.service.ProductoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/productos/all")
    public List<Producto> obtenerProductos() {
        return productoService.obtenerProductos();
    }
}