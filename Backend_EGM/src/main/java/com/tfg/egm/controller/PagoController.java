package com.tfg.egm.controller;

import com.tfg.egm.entity.Cliente;
import com.tfg.egm.entity.Pago;
import com.tfg.egm.service.ClienteService;
import com.tfg.egm.service.PagoService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PagoController {

    private final PagoService pagoService;
    private final ClienteService clienteService;

    public PagoController(PagoService pagoService , ClienteService clienteService) {
        this.pagoService = pagoService;
        this.clienteService = clienteService;
    }

    @GetMapping("/pagos/all")
    public List<Pago> obtenerPagos() {
        return pagoService.obtenerPagos();
    }

    @GetMapping("/pagos/find/{id}")
    public ResponseEntity<Pago> obtenerPagoPorId(@PathVariable Long id) {
        try {
            Pago pago = pagoService.obtenerPagoPorId(id);
            return ResponseEntity.ok(pago);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/pagos/cliente/{user}")
    public List<Pago> obtenerPagosPorCliente(@PathVariable String user) {
        return pagoService.obtenerPagos(clienteService.obtenerClientePorUsuario(user));
    }

    @GetMapping("/pagos/admin-busqueda/{texto}")
    public ResponseEntity<List<Pago>> obtenerPagosFiltroAdmin(@PathVariable String texto) {
        try {
            List<Pago> pagos = pagoService.buscarPagoAdmin(texto);
            return ResponseEntity.ok(pagos);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/pagos/new/{user}")
    public ResponseEntity<Pago> crearPago(@PathVariable String user, @RequestBody Pago pago) {
        Cliente cliente = clienteService.obtenerClientePorUsuario(user);
        pago.setCliente(cliente);
        Pago nuevoPago = pagoService.save(pago);
        return ResponseEntity.ok(nuevoPago);
    }

    @PutMapping("/pagos/update/{id}")
    public ResponseEntity<Pago> actualizarPago(@PathVariable Long id, @RequestBody Pago pago) {
        pagoService.actualizarPago(id, pago);
        return ResponseEntity.ok(pago);
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

    @DeleteMapping("/pagos/disable/{id}")
    public ResponseEntity<Void> disablePago(@PathVariable Long id) {
        try {
            pagoService.disablePago(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/pagos/enable/{id}")
    public ResponseEntity<Void> enablePago(@PathVariable Long id) {
        try {
            pagoService.enablePago(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}