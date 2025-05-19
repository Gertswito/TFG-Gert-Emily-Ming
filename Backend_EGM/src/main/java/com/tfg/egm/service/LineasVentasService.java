package com.tfg.egm.service;

import com.tfg.egm.entity.LineasVentas;
import com.tfg.egm.repository.LineasVentasRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Servicio para la gestión de líneas de venta.
 * Proporciona métodos para obtener, buscar, crear, actualizar y eliminar líneas de venta.
 */
@Service
public class LineasVentasService {

    private final LineasVentasRepository lineasVentasRepository;

    private final ProductoService productoService;

    /**
     * Constructor que inyecta el repositorio de líneas de venta y el servicio de productos.
     * @param lineasVentasRepository repositorio de líneas de venta
     * @param productoService servicio de productos
     */
    public LineasVentasService(LineasVentasRepository lineasVentasRepository, ProductoService productoService) {
        this.lineasVentasRepository = lineasVentasRepository;
        this.productoService = productoService;
    }

    /**
     * Obtiene la lista de todas las líneas de venta.
     * @return lista de líneas de venta
     */
    public List<LineasVentas> obtenerLineasVentas() {
        return lineasVentasRepository.findAll();
    }

    /**
     * Obtiene una línea de venta por su ID.
     * @param id identificador de la línea de venta
     * @return la línea de venta encontrada
     * @throws ResponseStatusException si no existe la línea de venta
     */
    public LineasVentas obtenerLineaVentaPorId(Long id) {
        return lineasVentasRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "subcategoriaNoExiste"));
    }

    /**
     * Obtiene todas las líneas de venta asociadas a una venta.
     * @param id identificador de la venta
     * @return lista de líneas de venta de esa venta
     */
    public List<LineasVentas> obtenerLineasVentasPorVenta(Long id) {
        return lineasVentasRepository.findByVentaId(id);
    }

    /**
     * Busca líneas de venta para administración filtrando por varios campos.
     * @param texto texto de búsqueda
     * @return lista de líneas de venta encontradas
     */
    public List<LineasVentas> buscarLineaVentaAdmin(String texto) {
        return lineasVentasRepository.buscarLineaVentaAdmin(texto);
    }

    /**
     * Elimina una línea de venta por su ID.
     * @param id identificador de la línea de venta
     * @throws ResponseStatusException si no existe la línea de venta o no se puede eliminar
     */
    public void deleteLineasVentas(Long id) {
        if (!lineasVentasRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "lineasVentaNoExiste");
        }
        try {
            lineasVentasRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "lineasVentaNoSePuedeEliminar", e);
        }
    }

    /**
     * Guarda una nueva línea de venta.
     * @param lineasVentas objeto línea de venta a guardar
     * @return la línea de venta guardada
     */
    public LineasVentas save(LineasVentas lineasVentas) {
        return lineasVentasRepository.save(lineasVentas);
    }

    /**
     * Actualiza una línea de venta existente.
     * @param lineasVentas objeto línea de venta con los nuevos datos
     * @return la línea de venta actualizada
     */
    public LineasVentas update(LineasVentas lineasVentas) {
        return lineasVentasRepository.save(lineasVentas);
    }

    /**
     * Guarda una línea de venta y actualiza el stock del producto correspondiente.
     * @param lineaVenta objeto línea de venta a guardar
     * @return la línea de venta guardada
     */
    public LineasVentas guardarLineaYCalcularStock(LineasVentas lineaVenta) {
        productoService.restarStockProducto(lineaVenta); 
        return lineasVentasRepository.save(lineaVenta);
    }
    
}