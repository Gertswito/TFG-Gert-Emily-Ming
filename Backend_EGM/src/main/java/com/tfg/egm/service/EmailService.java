package com.tfg.egm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * Servicio encargado del envío de correos electrónicos, tanto simples como con archivos adjuntos.
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Envía un correo electrónico simple sin adjuntos.
     *
     * @param para dirección de correo del destinatario
     * @param asunto asunto del correo
     * @param cuerpo contenido del mensaje
     */
    public void enviarCorreo(String para, String asunto, String cuerpo) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(para);
        mensaje.setSubject(asunto);
        mensaje.setText(cuerpo);
        mensaje.setFrom("noreply.tiendafresma@gmail.com");

        mailSender.send(mensaje);
    }

    /**
     * Envía un correo electrónico con un archivo adjunto en formato byte array.
     *
     * @param para dirección de correo del destinatario
     * @param asunto asunto del correo
     * @param cuerpo contenido del mensaje
     * @param archivoBytes archivo adjunto en forma de array de bytes
     * @param nombreArchivo nombre del archivo adjunto
     * @throws MessagingException si ocurre un error al adjuntar o enviar el correo
     */
    public void enviarCorreoConAdjuntoBytes(String para, String asunto, String cuerpo, byte[] archivoBytes, String nombreArchivo) throws MessagingException {
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

        helper.setTo(para);
        helper.setSubject(asunto);
        helper.setText(cuerpo);
        helper.setFrom("noreply.tiendafresma@gmail.com");

        ByteArrayResource recurso = new ByteArrayResource(archivoBytes);
        helper.addAttachment(nombreArchivo, recurso);

        mailSender.send(mensaje);
    }
}