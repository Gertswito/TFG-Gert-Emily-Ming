package com.tfg.egm.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.tfg.egm.entity.LineasVentas;
import com.tfg.egm.entity.Venta;

import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio encargado de generar facturas en formato PDF a partir de los datos de una venta.
 * Utiliza Thymeleaf para procesar la plantilla HTML y OpenHTMLtoPDF para la conversión a PDF.
 */
@Service
public class FacturaPdfService {

    private final SpringTemplateEngine templateEngine;

    private final LineasVentasService lineasVentasService;

    /**
     * Constructor que inyecta las dependencias necesarias para la generación de facturas PDF.
     * 
     * @param templateEngine motor de plantillas Thymeleaf
     * @param lineasVentasService servicio para obtener las líneas de venta asociadas a una venta
     */
    public FacturaPdfService(SpringTemplateEngine templateEngine, LineasVentasService lineasVentasService) {
        this.templateEngine = templateEngine;
        this.lineasVentasService = lineasVentasService;
    }

    /**
     * Genera un archivo PDF de la factura correspondiente a la venta indicada.
     *
     * @param venta la venta para la cual se generará el PDF
     * @return un array de bytes que representa el PDF generado
     * @throws IllegalArgumentException si la venta o sus líneas no existen
     * @throws RuntimeException si ocurre un error durante la generación del PDF
     */
    public byte[] generarFacturaPdf(Venta venta) {
        if (venta == null) {
            throw new IllegalArgumentException("Venta no encontrada con ID: " + venta.getId());
        }

        List<LineasVentas> lineasVentas = lineasVentasService.obtenerLineasVentasPorVenta(venta.getId().longValue());
        if (lineasVentas == null || lineasVentas.isEmpty()) {
            throw new IllegalArgumentException("No hay líneas de venta asociadas a la venta con ID: " + venta.getId());
        }

        Context context = new Context();
        context.setVariable("ventaId", venta.getId());
        context.setVariable("clienteNombre", venta.getCliente().getNombre() + " " + venta.getCliente().getApellidos());
        context.setVariable("clienteUsuario", venta.getCliente().getUsuario());
        context.setVariable("fechaHora", venta.getFechaHora().toString());
        context.setVariable("precioFinal", venta.getPrecioFinal());
        context.setVariable("direccion", venta.getDireccion().getDireccion() + ", " + venta.getDireccion().getLocalidad() + ", " + venta.getDireccion().getComunidadAutonoma());
        context.setVariable("codigoPostal", venta.getDireccion().getCodigoPostal());
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String fechaHoraFormateada = venta.getFechaHora().format(formatter);
        context.setVariable("fechaHora", fechaHoraFormateada);

        String numeroTarjetaStr = venta.getPago() != null && venta.getPago().getNumeroTarjeta() != null
            ? "**** **** **** " + String.format("%04d", venta.getPago().getNumeroTarjeta() % 10000)
            : "No disponible";
        context.setVariable("numeroTarjeta", numeroTarjetaStr);
        String fechaCaducidadStr = venta.getPago() != null && venta.getPago().getFechaCaducidad() != null
            ? venta.getPago().getFechaCaducidad().format(DateTimeFormatter.ofPattern("MM-yyyy"))
            : "No disponible";
        context.setVariable("fechaCaducidad", fechaCaducidadStr);

        List<Map<String, Object>> productos = lineasVentas.stream().map(linea -> {
            Map<String, Object> map = new HashMap<>();
            map.put("referencia", linea.getProducto().getReferencia());
            map.put("marca", linea.getProducto().getMarca());
            map.put("nombre", linea.getProducto().getNombre());
            map.put("cantidadPedida", linea.getCantidadPedida());
            map.put("precioUnitario", linea.getPrecioUnitario());
            map.put("precioTotal", linea.getPrecioTotal());
            return map;
        }).toList();
        context.setVariable("productos", productos);

        String html = templateEngine.process("factura", context);

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();
            return os.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF", e);
        }
    }
}