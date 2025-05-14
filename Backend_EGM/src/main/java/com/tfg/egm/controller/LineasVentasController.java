package com.tfg.egm.controller;

import com.tfg.egm.entity.LineasVentas;
import com.tfg.egm.entity.Subcategoria;
import com.tfg.egm.service.LineasVentasService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/lineas-ventas/find/{id}")
    public ResponseEntity<LineasVentas> obtenerLineaVentaPorId(@PathVariable Long id) {
        try {
            LineasVentas lineaVenta = lineasVentasService.obtenerLineaVentaPorId(id);
            return ResponseEntity.ok(lineaVenta);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/lineas-ventas/venta/{id}")
    public ResponseEntity<List<LineasVentas>> obtenerLineasVentasPorVenta(@PathVariable Long id) {
        try {
            List<LineasVentas> lineasVentas = lineasVentasService.obtenerLineasVentasPorVenta(id);
            return ResponseEntity.ok(lineasVentas);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/lineas-ventas/admin-busqueda/{texto}")
    public ResponseEntity<List<LineasVentas>> obtenerLineasVentasFiltroAdmin(@PathVariable String texto) {
        try {
            List<LineasVentas> lineasVentas = lineasVentasService.buscarLineaVentaAdmin(texto);
            return ResponseEntity.ok(lineasVentas);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
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

    @PostMapping("/lineas-ventas/new")
    public ResponseEntity<Object> createLineasVentas(@RequestBody LineasVentas lineasVentas) throws URISyntaxException {
        try {
            LineasVentas nuevaLineasVentas = lineasVentasService.save(lineasVentas);
            URI location = new URI("/categorias/new/" + nuevaLineasVentas.getId());
            return ResponseEntity.created(location).body(nuevaLineasVentas);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("error", ex.getReason()));
        }
    }

    @PutMapping("/lineas-ventas/update/{id}")
    public ResponseEntity<LineasVentas> updateLineasVentas(@PathVariable Long id, @RequestBody LineasVentas lineasVentas) {
        try {
            if (lineasVentasService.obtenerLineaVentaPorId(id) != null) {
                lineasVentasService.update(lineasVentas);
                return ResponseEntity.ok(lineasVentas);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (ResponseStatusException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
