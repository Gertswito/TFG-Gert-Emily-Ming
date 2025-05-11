package com.tfg.egm.service;

import com.tfg.egm.entity.Producto;
import com.tfg.egm.entity.Subcategoria;
import com.tfg.egm.repository.ProductoRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> obtenerProductos() {
        return productoRepository.findAll();
    }

    public List<Producto> obtenerProductosConIdSubcategoria(int id) {
        return productoRepository.findBySubcategoriaId(id);
    }

    public Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "productoNoExiste"));
    }

    public List<Producto> buscarPorNombreProducto(String texto) {
        return productoRepository.buscarPorNombreProducto(texto);
    }

    public List<Producto> buscarPorNombreProductoIdSubcategoria(String texto, Long id) {
        return productoRepository.buscarPorNombreProductoIdSubcategoria(texto, id);
    }

    public List<Producto> buscarProductoAdmin(String texto) {
        return productoRepository.buscarProductoAdmin(texto);
    }
    
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

    public Producto save(Producto producto) {
        if (productoRepository.existsByReferencia(producto.getReferencia())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "referenciaExiste");
        }
        return productoRepository.save(producto);
    }

    public Producto update(Producto producto) {
        return productoRepository.save(producto);
    }
}