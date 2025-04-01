package com.tfg.egm.service;

import com.tfg.egm.entity.Producto;
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
}