package com.tfg.egm.controller;

import com.tfg.egm.entity.Pago;
import com.tfg.egm.service.PagoService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping("/pagos/all")
    public List<Pago> obtenerPagos() {
        return pagoService.obtenerPagos();
    }
    
    @DeleteMapping("/pagos/delete/{id}")
    public ResponseEntity<Void> deletePago(@PathVariable Long id) {
        try {
            pagoService.deletePago(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}