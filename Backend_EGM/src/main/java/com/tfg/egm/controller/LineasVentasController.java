package com.tfg.egm.controller;

import com.tfg.egm.entity.LineasVentas;
import com.tfg.egm.service.LineasVentasService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LineasVentasController {

    private final LineasVentasService lineasVentasService;

    public LineasVentasController(LineasVentasService lineasVentasService) {
        this.lineasVentasService = lineasVentasService;
    }

    @GetMapping("/lineas-ventas/all")
    public List<LineasVentas> obtenerLineasVentas() {
        return lineasVentasService.obtenerLineasVentas();
    }
}
