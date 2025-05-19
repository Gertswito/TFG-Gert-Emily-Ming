package com.tfg.egm.controller;

import com.tfg.egm.entity.Producto;
import com.tfg.egm.service.ProductoService;

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
 * Controlador REST para gestionar los productos.
 * Permite obtener, buscar, crear, actualizar y eliminar productos.
 */
@RestController
public class ProductoController {

    private final ProductoService productoService;

    /**
     * Constructor que inyecta el servicio de productos.
     * @param productoService servicio de productos
     */
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    /**
     * Obtiene la lista de todos los productos.
     * @return lista de productos
     */
    @GetMapping("/productos/all")
    public List<Producto> obtenerProductos() {
        return productoService.obtenerProductos();
    }

    /**
     * Obtiene los productos de una subcategoría por su ID.
     * @param id identificador de la subcategoría
     * @return lista de productos de la subcategoría
     */
    @GetMapping("/productos/subcategoria/{id}")
    public List<Producto> obtenerProductosConIdSubcategoria(@PathVariable int id) {
        return productoService.obtenerProductosConIdSubcategoria(id);
    }

    /**
     * Obtiene un producto por su ID.
     * @param id identificador del producto
     * @return ResponseEntity con el producto o 404 si no se encuentra
     */
    @GetMapping("/productos/find/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id) {
        try {
            Producto producto = productoService.obtenerProductoPorId(id);
            return ResponseEntity.ok(producto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Busca productos por nombre.
     * @param texto texto de búsqueda
     * @return ResponseEntity con la lista de productos encontrados o 404
     */
    @GetMapping("/productos/busqueda/{texto}")
    public ResponseEntity<List<Producto>> obtenerProductoFiltro(@PathVariable String texto) {
        try {
            List<Producto> productos = productoService.buscarPorNombreProducto(texto);
            return ResponseEntity.ok(productos);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Busca productos por nombre y subcategoría.
     * @param texto texto de búsqueda
     * @param id identificador de la subcategoría
     * @return ResponseEntity con la lista de productos encontrados o 404
     */
    @GetMapping("/productos/busqueda/{texto}/subcategoria/{id}")
    public ResponseEntity<List<Producto>> obtenerProductoFiltroSubcategoria(@PathVariable String texto, @PathVariable Long id) {
        try {
            List<Producto> productos = productoService.buscarPorNombreProductoIdSubcategoria(texto, id);
            return ResponseEntity.ok(productos);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Busca productos para administración.
     * @param texto texto de búsqueda
     * @return ResponseEntity con la lista de productos encontrados o 404
     */
    @GetMapping("/productos/admin-busqueda/{texto}")
    public ResponseEntity<List<Producto>> obtenerProductoFiltroAdmin(@PathVariable String texto) {
        try {
            List<Producto> productos = productoService.buscarProductoAdmin(texto);
            return ResponseEntity.ok(productos);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Elimina un producto por su ID.
     * @param id identificador del producto
     * @return ResponseEntity sin contenido o 404 si no se encuentra
     */
    @DeleteMapping("/productos/delete/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        try {
            productoService.deleteProducto(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crea un nuevo producto.
     * @param producto objeto producto a crear
     * @return ResponseEntity con el nuevo producto y la ubicación
     * @throws URISyntaxException si la URI no es válida
     */
    @PostMapping("/productos/new")
    public ResponseEntity<Object> createProducto(@RequestBody Producto producto) throws URISyntaxException {
        try {
            Producto nuevaProducto = productoService.save(producto);
            URI location = new URI("/categorias/new/" + nuevaProducto.getId());
            return ResponseEntity.created(location).body(nuevaProducto);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("error", ex.getReason()));
        }
    }

    /**
     * Actualiza un producto existente.
     * @param id identificador del producto
     * @param producto objeto producto con los nuevos datos
     * @return ResponseEntity con el producto actualizado o 404 si no se encuentra
     */
    @PutMapping("/productos/update/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long id, @RequestBody Producto producto) {
        try {
            if (productoService.obtenerProductoPorId(id) != null) {
                productoService.update(producto);
                return ResponseEntity.ok(producto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (ResponseStatusException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}