package com.tfg.egm.controller;

import com.tfg.egm.entity.Direccion;
import com.tfg.egm.service.DireccionService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DireccionController {

    private final DireccionService direccionService;

    public DireccionController(DireccionService direccionService) {
        this.direccionService = direccionService;
    }

    @GetMapping("/direcciones/all")
    public List<Direccion> obtenerDirecciones() {
        return direccionService.obtenerDirecciones();
    }
    
    @DeleteMapping("/direcciones/delete/{id}")
    public ResponseEntity<Void> deleteDireccion(@PathVariable Long id) {
        try {
            direccionService.deleteDireccion(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}