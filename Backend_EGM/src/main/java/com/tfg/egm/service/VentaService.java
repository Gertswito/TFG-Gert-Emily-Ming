package com.tfg.egm.service;

import com.tfg.egm.entity.LineasVentas;
import com.tfg.egm.entity.Producto;
import com.tfg.egm.entity.Venta;
import com.tfg.egm.repository.ProductoRepository;
import com.tfg.egm.repository.VentaRepository;

import jakarta.transaction.Transactional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio para la gestión de ventas.
 * Proporciona métodos para obtener, buscar, crear, actualizar, eliminar ventas y finalizar compras.
 */
@Service
public class VentaService {

    private final VentaRepository ventaRepository;

    private final LineasVentasService lineasVentasService;

    private final ProductoRepository productoRepository;

    /**
     * Constructor que inyecta los repositorios y servicios necesarios.
     * @param ventaRepository repositorio de ventas
     * @param lineasVentasService servicio de líneas de venta
     * @param productoRepository repositorio de productos
     */
    public VentaService(VentaRepository ventaRepository, LineasVentasService lineasVentasService, ProductoRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.lineasVentasService = lineasVentasService;
        this.productoRepository = productoRepository;
    }

    /**
     * Obtiene la lista de todas las ventas.
     * @return lista de ventas
     */
    public List<Venta> obtenerVentas() {
        return ventaRepository.findAll();
    }

    /**
     * Obtiene una venta por su ID.
     * @param id identificador de la venta
     * @return la venta encontrada
     * @throws ResponseStatusException si no existe la venta
     */
    public Venta obtenerVentaPorId(Long id) {
        return ventaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ventaNoExiste"));
    }

    /**
     * Busca ventas para administración filtrando por varios campos.
     * @param texto texto de búsqueda
     * @return lista de ventas encontradas
     */
    public List<Venta> buscarVentaAdmin(String texto) {
        return ventaRepository.buscarVentaAdmin(texto);
    }

    /**
     * Obtiene todas las ventas asociadas a un cliente por su ID.
     * @param id identificador del cliente
     * @return lista de ventas del cliente
     */
    public List<Venta> obtenerVentasPorCliente(Long id) {
        return ventaRepository.findByClienteId(id);
    }

    /**
     * Elimina una venta por su ID.
     * @param id identificador de la venta
     * @throws ResponseStatusException si no existe la venta o no se puede eliminar
     */
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

    /**
     * Guarda una nueva venta. Si no se indica fecha, se asigna la fecha y hora actual.
     * @param venta objeto venta a guardar
     * @return la venta guardada
     */
    public Venta save(Venta venta) {
        if(venta.getFechaHora() == null) {
            LocalDateTime fechaHoy = LocalDateTime.now();
            venta.setFechaHora(fechaHoy);
        }
        return ventaRepository.save(venta);
    }

    /**
     * Actualiza una venta existente. Valida que la fecha no sea futura.
     * @param venta objeto venta con los nuevos datos
     * @return la venta actualizada
     * @throws ResponseStatusException si la fecha es inválida
     */
    public Venta update(Venta venta) {
        if (venta.getFechaHora() != null) {
            LocalDateTime fechaHoy = LocalDateTime.now();
            if (venta.getFechaHora().isAfter(fechaHoy)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fechaInvalida");
            }
        }
        return ventaRepository.save(venta);
    }

    /**
     * Finaliza una compra, validando el stock de los productos y guardando la venta y sus líneas.
     * Si hay errores de stock, los devuelve en el resultado.
     * @param venta objeto venta a finalizar
     * @return mapa con la venta creada o con los errores encontrados
     */
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

    /**
     * Valida que el producto de la línea de venta tenga suficiente stock.
     * @param lineaVenta línea de venta a validar
     * @throws ResponseStatusException si no existe el producto o no hay suficiente stock
     */
    public void validarStock(LineasVentas lineaVenta) {
        Producto producto = productoRepository.findById(lineaVenta.getProducto().getId().longValue())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "productoNoExiste"));
        if (producto.getStock() < lineaVenta.getCantidadPedida()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "stockInsuficiente");
        }
    }
}