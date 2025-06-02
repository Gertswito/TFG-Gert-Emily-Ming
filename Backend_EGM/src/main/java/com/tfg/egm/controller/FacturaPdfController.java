package com.tfg.egm.controller;

import com.tfg.egm.service.FacturaPdfService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ContentDisposition;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la gestión y descarga de facturas en formato PDF.
 * Proporciona un endpoint para obtener la factura PDF correspondiente a una venta.
 */
@RestController
@RequestMapping("/api/facturas")
public class FacturaPdfController {

    private final FacturaPdfService facturaPdfService;

    /**
     * Constructor que inyecta el servicio de generación de facturas PDF.
     * @param facturaPdfService servicio encargado de generar el PDF de la factura
     */
    public FacturaPdfController(FacturaPdfService facturaPdfService) {
        this.facturaPdfService = facturaPdfService;
    }

    /**
     * Descarga la factura en formato PDF correspondiente al ID de la venta proporcionado.
     *
     * @param ventaId identificador de la venta
     * @return ResponseEntity con el PDF de la factura y los encabezados adecuados
     */
    @GetMapping(value = "/{ventaId}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> descargarFactura(@PathVariable Long ventaId) {
        byte[] pdfBytes = facturaPdfService.generarFacturaPdf(ventaId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename("factura_" + ventaId + "_Tienda_Fresma.pdf")
                .build());

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}