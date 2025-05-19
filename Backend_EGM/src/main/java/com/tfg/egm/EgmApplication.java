package com.tfg.egm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación EGM.
 * Inicia la aplicación Spring Boot.
 */
@SpringBootApplication
public class EgmApplication {

    /**
     * Método principal que lanza la aplicación Spring Boot.
     * @param args argumentos de la línea de comandos
     */
	public static void main(String[] args) {
		SpringApplication.run(EgmApplication.class, args);
	}

}
