package com.tfg.egm.controller;

import com.tfg.egm.entity.Venta;
import com.tfg.egm.service.LineasVentasService;
import com.tfg.egm.service.VentaService;

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
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService, LineasVentasService lineasVentasService) {
        this.ventaService = ventaService;
    }

    @GetMapping("/ventas/all")
    public List<Venta> obtenerVentas() {
        return ventaService.obtenerVentas();
    }

    @GetMapping("/ventas/find/{id}")
    public ResponseEntity<Venta> obtenerVentaPorId(@PathVariable Long id) {
        try {
            Venta venta = ventaService.obtenerVentaPorId(id);
            return ResponseEntity.ok(venta);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ventas/admin-busqueda/{texto}")
    public ResponseEntity<List<Venta>> obtenerVentaFiltroAdmin(@PathVariable String texto) {
        try {
            List<Venta> ventas = ventaService.buscarVentaAdmin(texto);
            return ResponseEntity.ok(ventas);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/ventas/delete/{id}")
    public ResponseEntity<Void> deleteVenta(@PathVariable Long id) {
        try {
            ventaService.deleteVenta(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/ventas/new")
    public ResponseEntity<Object> createVenta(@RequestBody Venta venta) throws URISyntaxException {
        try {
            Venta nuevaVenta = ventaService.save(venta);
            URI location = new URI("/venta/new/" + nuevaVenta.getId());
            return ResponseEntity.created(location).body(nuevaVenta);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("error", ex.getReason()));
        }
    }

    @PutMapping("/ventas/update/{id}")
    public ResponseEntity<Venta> updateVenta(@PathVariable Long id, @RequestBody Venta venta) {
        try {
            if (ventaService.obtenerVentaPorId(id) != null) {
                ventaService.update(venta);
                return ResponseEntity.ok(venta);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (ResponseStatusException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/ventas/finalizar-compra")
    public ResponseEntity<Object> finalizarCompra(@RequestBody Venta venta) {
        Map<String, Object> resultado = ventaService.finalizarCompra(venta);
    
        if (resultado.containsKey("errores")) {
            return ResponseEntity.badRequest().body(resultado.get("errores")); // devuelve el array JSON
        }
    
        Venta nuevaVenta = (Venta) resultado.get("venta");
        URI location = URI.create("/venta/new/" + nuevaVenta.getId());
        return ResponseEntity.created(location).body(nuevaVenta);
    }
}