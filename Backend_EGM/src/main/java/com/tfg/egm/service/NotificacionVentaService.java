package com.tfg.egm.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.tfg.egm.entity.LineasVentas;
import com.tfg.egm.entity.Venta;

import jakarta.mail.MessagingException;

/**
 * Servicio encargado de gestionar el envío de notificaciones por correo electrónico
 * relacionadas con las ventas, incluyendo el envío de la factura en PDF al cliente.
 */
@Service
public class NotificacionVentaService {

    private final LineasVentasService lineasVentasService;

    private final FacturaPdfService facturaPdfService;

    private final EmailService emailService;

    /**
     * Constructor que inyecta los servicios necesarios para la notificación de ventas.
     *
     * @param lineasVentasService servicio para obtener las líneas de venta
     * @param facturaPdfService servicio para generar la factura en PDF
     * @param emailService servicio para el envío de correos electrónicos
     */
    public NotificacionVentaService(
        LineasVentasService lineasVentasService,
        FacturaPdfService facturaPdfService,
        EmailService emailService
    ) {
        this.lineasVentasService = lineasVentasService;
        this.facturaPdfService = facturaPdfService;
        this.emailService = emailService;
    }

    /**
     * Envía un correo electrónico al cliente con la factura de la venta en formato PDF adjunta.
     * Si la venta no tiene líneas asociadas, lanza una excepción.
     *
     * @param venta la venta para la cual se enviará la notificación
     * @throws RuntimeException si ocurre un error al enviar el correo
     */
    public void enviarCorreoVenta(Venta venta) {
        try {
            List<LineasVentas> lineasVentas = lineasVentasService.obtenerLineasVentasPorVenta(venta.getId().longValue());
            if (lineasVentas.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ventaSinLineas");
            }

            byte[] pdfBytes = facturaPdfService.generarFacturaPdf(venta);

            String emailDestinatario = venta.getCliente().getEmail();
            String asunto = "Factura de su compra #" + venta.getId();
            String nombre = venta.getCliente().getNombre() + " " + venta.getCliente().getApellidos();
            String cuerpo = String.format("Hola %s,\n\nSu compra ha sido realizada con éxito. Muchas gracias por confiar en nuestra tienda. Le hemos adjuntado la factura de su pedido.\n\nAtentamente, el equipo de Tienda Fresma", nombre);

            emailService.enviarCorreoConAdjuntoBytes(emailDestinatario, asunto, cuerpo, pdfBytes, "factura_" + venta.getId() + "_Tienda_Fresma.pdf");
        } catch (MessagingException e) {
            throw new RuntimeException("Error enviando correo", e);
        }
    }
}