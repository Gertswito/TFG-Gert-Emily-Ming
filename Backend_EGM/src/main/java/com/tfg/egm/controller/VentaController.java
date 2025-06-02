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

/**
 * Controlador REST para gestionar las ventas.
 * Permite obtener, buscar, crear, actualizar, eliminar ventas y finalizar compras.
 */
@RestController
public class VentaController {

    private final VentaService ventaService;

    /**
     * Constructor que inyecta los servicios de venta y líneas de venta.
     * @param ventaService servicio de ventas
     * @param lineasVentasService servicio de líneas de venta
     */
    public VentaController(VentaService ventaService, LineasVentasService lineasVentasService) {
        this.ventaService = ventaService;
    }

    /**
     * Obtiene la lista de todas las ventas.
     * @return lista de ventas
     */
    @GetMapping("/ventas/all")
    public List<Venta> obtenerVentas() {
        return ventaService.obtenerVentas();
    }

    /**
     * Obtiene una venta por su ID.
     * @param id identificador de la venta
     * @return ResponseEntity con la venta o 404 si no se encuentra
     */
    @GetMapping("/ventas/find/{id}")
    public ResponseEntity<Venta> obtenerVentaPorId(@PathVariable Long id) {
        try {
            Venta venta = ventaService.obtenerVentaPorId(id);
            return ResponseEntity.ok(venta);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Busca ventas para administración.
     * @param texto texto de búsqueda
     * @return ResponseEntity con la lista de ventas encontradas o 404
     */
    @GetMapping("/ventas/admin-busqueda/{texto}")
    public ResponseEntity<List<Venta>> obtenerVentaFiltroAdmin(@PathVariable String texto) {
        try {
            List<Venta> ventas = ventaService.buscarVentaAdmin(texto);
            return ResponseEntity.ok(ventas);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene todas las ventas de un cliente por su ID.
     * @param id identificador del cliente
     * @return ResponseEntity con la lista de ventas o 404 si no se encuentra
     */
    @GetMapping("/ventas/cliente/{id}")
    public ResponseEntity<List<Venta>> obtenerVentasPorCliente(@PathVariable Long id) {
        try {
            List<Venta> ventas = ventaService.obtenerVentasPorCliente(id);
            return ResponseEntity.ok(ventas);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Elimina una venta por su ID.
     * @param id identificador de la venta
     * @return ResponseEntity sin contenido o 404 si no se encuentra
     */
    @DeleteMapping("/ventas/delete/{id}")
    public ResponseEntity<Void> deleteVenta(@PathVariable Long id) {
        try {
            ventaService.deleteVenta(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crea una nueva venta.
     * @param venta objeto venta a crear
     * @return ResponseEntity con la nueva venta y la ubicación
     * @throws URISyntaxException si la URI no es válida
     */
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

    /**
     * Actualiza una venta existente.
     * @param id identificador de la venta
     * @param venta objeto venta con los nuevos datos
     * @return ResponseEntity con la venta actualizada o 404 si no se encuentra
     */
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

    /**
     * Finaliza una compra, creando la venta, enviando un email y devolviendo el resultado.
     * @param venta objeto venta a finalizar
     * @return ResponseEntity con la nueva venta o errores si los hay
     */
    @PostMapping("/ventas/finalizar-compra")
    public ResponseEntity<Object> finalizarCompra(@RequestBody Venta venta) {
        Map<String, Object> resultado = ventaService.finalizarCompra(venta);
    
        if (resultado.containsKey("errores")) {
            return ResponseEntity.badRequest().body(resultado.get("errores"));
        }
    
        Venta nuevaVenta = (Venta) resultado.get("venta");
        ventaService.enviarCorreoVenta(nuevaVenta);
        URI location = URI.create("/venta/new/" + nuevaVenta.getId());
        return ResponseEntity.created(location).body(nuevaVenta);
    }
}