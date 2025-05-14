package com.tfg.egm.service;

import com.tfg.egm.entity.Categoria;
import com.tfg.egm.entity.LineasVentas;
import com.tfg.egm.entity.Producto;
import com.tfg.egm.entity.Subcategoria;
import com.tfg.egm.entity.Venta;
import com.tfg.egm.repository.ProductoRepository;
import com.tfg.egm.repository.VentaRepository;

import jakarta.transaction.Transactional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;

    private final LineasVentasService lineasVentasService;

    private final ProductoRepository productoRepository;

    public VentaService(VentaRepository ventaRepository, LineasVentasService lineasVentasService, ProductoRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.lineasVentasService = lineasVentasService;
        this.productoRepository = productoRepository;
    }

    public List<Venta> obtenerVentas() {
        return ventaRepository.findAll();
    }

    public Venta obtenerVentaPorId(Long id) {
        return ventaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ventaNoExiste"));
    }

    public List<Venta> buscarVentaAdmin(String texto) {
        return ventaRepository.buscarVentaAdmin(texto);
    }

    public List<Venta> obtenerVentasPorCliente(Long id) {
        return ventaRepository.findByClienteId(id);
    }

    public void deleteVenta(Long id) {
        if (!ventaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ventaNoExiste");
        }
        try {
            ventaRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "ventaNoSePuedeEliminar", e);
        }
    }

    public Venta save(Venta venta) {
        if(venta.getFechaHora() == null) {
            LocalDateTime fechaHoy = LocalDateTime.now();
            venta.setFechaHora(fechaHoy);
        }
        return ventaRepository.save(venta);
    }

    public Venta update(Venta venta) {
        if (venta.getFechaHora() != null) {
            LocalDateTime fechaHoy = LocalDateTime.now();
            if (venta.getFechaHora().isAfter(fechaHoy)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fechaInvalida");
            }
        }
        return ventaRepository.save(venta);
    }

    @Transactional
    public Map<String, Object> finalizarCompra(Venta venta) {
        List<Map<String, Object>> errores = new ArrayList<>();
    
        for (LineasVentas linea : venta.getLineasVentas()) {
            try {
                validarStock(linea);
            } catch (ResponseStatusException ex) {
                Producto producto = productoRepository.findById(linea.getProducto().getId().longValue())
                    .orElse(null);
                String nombreCompleto = (producto != null)
                    ? producto.getMarca() + " " + producto.getNombre()
                    : "Producto desconocido";
                Map<String, Object> error = new HashMap<>();
                error.put("productoId", linea.getProducto().getId());
                error.put("productoNombre", nombreCompleto);
                error.put("error", ex.getReason());
                errores.add(error);
            }
        }
        if (!errores.isEmpty()) {
            return Map.of("errores", errores); 
        }

        if (venta.getFechaHora() == null) {
            venta.setFechaHora(LocalDateTime.now());
        }
        Venta nuevaVenta = ventaRepository.save(venta);
        for (LineasVentas linea : venta.getLineasVentas()) {
            linea.setVenta(nuevaVenta);
            lineasVentasService.guardarLineaYCalcularStock(linea);
        }
        return Map.of("venta", nuevaVenta);
    }

    public void validarStock(LineasVentas lineaVenta) {
        Producto producto = productoRepository.findById(lineaVenta.getProducto().getId().longValue())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "productoNoExiste"));
        if (producto.getStock() < lineaVenta.getCantidadPedida()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "stockInsuficiente");
        }
    }
}