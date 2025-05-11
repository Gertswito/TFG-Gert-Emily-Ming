package com.tfg.egm.controller;

import com.tfg.egm.entity.Producto;
import com.tfg.egm.entity.Subcategoria;
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

@RestController
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/productos/all")
    public List<Producto> obtenerProductos() {
        return productoService.obtenerProductos();
    }

    @GetMapping("/productos/subcategoria/{id}")
    public List<Producto> obtenerProductosConIdSubcategoria(@PathVariable int id) {
        return productoService.obtenerProductosConIdSubcategoria(id);
    }

    @GetMapping("/productos/find/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id) {
        try {
            Producto producto = productoService.obtenerProductoPorId(id);
            return ResponseEntity.ok(producto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/productos/busqueda/{texto}")
    public ResponseEntity<List<Producto>> obtenerProductoFiltro(@PathVariable String texto) {
        try {
            List<Producto> productos = productoService.buscarPorNombreProducto(texto);
            return ResponseEntity.ok(productos);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/productos/busqueda/{texto}/subcategoria/{id}")
    public ResponseEntity<List<Producto>> obtenerProductoFiltroSubcategoria(@PathVariable String texto, @PathVariable Long id) {
        try {
            List<Producto> productos = productoService.buscarPorNombreProductoIdSubcategoria(texto, id);
            return ResponseEntity.ok(productos);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/productos/admin-busqueda/{texto}")
    public ResponseEntity<List<Producto>> obtenerProductoFiltroAdmin(@PathVariable String texto) {
        try {
            List<Producto> productos = productoService.buscarProductoAdmin(texto);
            return ResponseEntity.ok(productos);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/productos/delete/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        try {
            productoService.deleteProducto(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

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