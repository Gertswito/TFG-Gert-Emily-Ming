package com.tfg.egm.controller;

import com.tfg.egm.entity.Pago;
import com.tfg.egm.service.PagoService;
import org.springframework.web.bind.annotation.GetMapping;
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
}