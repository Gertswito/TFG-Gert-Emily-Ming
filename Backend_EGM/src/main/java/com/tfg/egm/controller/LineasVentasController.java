package com.tfg.egm.controller;

import com.tfg.egm.entity.LineasVentas;
import com.tfg.egm.service.LineasVentasService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @DeleteMapping("/lineas-ventas/delete/{id}")
    public ResponseEntity<Void> deleteLineasVentas(@PathVariable Long id) {
        try {
            lineasVentasService.deleteLineasVentas(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
