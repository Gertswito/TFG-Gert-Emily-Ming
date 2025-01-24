package com.tfg.egm.controller;

import com.tfg.egm.entity.Venta;
import com.tfg.egm.service.VentaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping("/ventas/all")
    public List<Venta> obtenerVentas() {
        return ventaService.obtenerVentas();
    }
}