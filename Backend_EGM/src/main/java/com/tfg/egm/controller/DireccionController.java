package com.tfg.egm.controller;

import com.tfg.egm.entity.Direccion;
import com.tfg.egm.service.DireccionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DireccionController {

    private final DireccionService direccionService;

    public DireccionController(DireccionService direccionService) {
        this.direccionService = direccionService;
    }

    @GetMapping("/direcciones/all")
    public List<Direccion> obtenerDirecciones() {
        return direccionService.obtenerDirecciones();
    }
}