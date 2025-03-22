package com.tfg.egm.controller;

import com.tfg.egm.entity.Cliente;
import com.tfg.egm.entity.Pago;
import com.tfg.egm.service.ClienteService;
import com.tfg.egm.service.PagoService;

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

    @GetMapping("/pagos/cliente/{user}")
    public List<Pago> obtenerPagos(@PathVariable String user) {
        return pagoService.obtenerPagos(clienteService.obtenerClientePorUsuario(user));
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
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
        pagoService.deletePago(id);
        return ResponseEntity.noContent().build(); // Devuelve un código 204 (sin contenido)
    }
}