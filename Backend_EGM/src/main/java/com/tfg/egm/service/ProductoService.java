package com.tfg.egm.service;

import com.tfg.egm.entity.LineasVentas;
import com.tfg.egm.entity.Producto;
import com.tfg.egm.repository.ProductoRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Servicio para la gestión de productos.
 * Proporciona métodos para obtener, buscar, crear, actualizar y eliminar productos, así como para gestionar el stock.
 */
@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    /**
     * Constructor que inyecta el repositorio de productos.
     * @param productoRepository repositorio de productos
     */
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * Obtiene la lista de todos los productos.
     * @return lista de productos
     */
    public List<Producto> obtenerProductos() {
        return productoRepository.findAll();
    }

    /**
     * Obtiene los productos de una subcategoría por su ID.
     * @param id identificador de la subcategoría
     * @return lista de productos de la subcategoría
     */
    public List<Producto> obtenerProductosConIdSubcategoria(int id) {
        return productoRepository.findBySubcategoriaId(id);
    }

    /**
     * Obtiene un producto por su ID.
     * @param id identificador del producto
     * @return el producto encontrado
     * @throws ResponseStatusException si no existe el producto
     */
    public Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "productoNoExiste"));
    }

    /**
     * Busca productos por nombre o marca.
     * @param texto texto de búsqueda
     * @return lista de productos encontrados
     */
    public List<Producto> buscarPorNombreProducto(String texto) {
        return productoRepository.buscarPorNombreProducto(texto);
    }

    /**
     * Busca productos por nombre o marca y por subcategoría.
     * @param texto texto de búsqueda
     * @param id identificador de la subcategoría
     * @return lista de productos encontrados
     */
    public List<Producto> buscarPorNombreProductoIdSubcategoria(String texto, Long id) {
        return productoRepository.buscarPorNombreProductoIdSubcategoria(texto, id);
    }

    /**
     * Busca productos para administración filtrando por varios campos.
     * @param texto texto de búsqueda
     * @return lista de productos encontrados
     */
    public List<Producto> buscarProductoAdmin(String texto) {
        return productoRepository.buscarProductoAdmin(texto);
    }
    
    /**
     * Elimina un producto por su ID.
     * @param id identificador del producto
     * @throws ResponseStatusException si no existe el producto o no se puede eliminar
     */
    public void deleteProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "productoNoExiste");
        }
        try {
            productoRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "productoNoSePuedeEliminar", e);
        }
    }

    /**
     * Guarda un nuevo producto.
     * @param producto objeto producto a guardar
     * @return el producto guardado
     * @throws ResponseStatusException si la referencia ya existe
     */
    public Producto save(Producto producto) {
        if (productoRepository.existsByReferencia(producto.getReferencia())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "referenciaExiste");
        }
        return productoRepository.save(producto);
    }

    /**
     * Actualiza un producto existente.
     * @param producto objeto producto con los nuevos datos
     * @return el producto actualizado
     */
    public Producto update(Producto producto) {
        return productoRepository.save(producto);
    }

    /**
     * Resta stock al producto correspondiente a una línea de venta.
     * @param lineaVenta línea de venta con la cantidad a restar
     * @throws ResponseStatusException si no existe el producto o no hay suficiente stock
     */
    public void restarStockProducto(LineasVentas lineaVenta) {
        Producto producto = productoRepository.findById(lineaVenta.getProducto().getId().longValue()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "productoNoExiste"));
        if (producto.getStock() < lineaVenta.getCantidadPedida()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "stockInsuficiente");
        }
        producto.setStock(producto.getStock() - lineaVenta.getCantidadPedida());
        productoRepository.save(producto);
    }
}