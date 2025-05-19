package com.tfg.egm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración global de CORS para la aplicación.
 * Permite peticiones desde http://localhost:4200 con varios métodos y credenciales.
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configura los mapeos CORS permitiendo solicitudes desde el frontend.
     *
     * @param registry objeto CorsRegistry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200") 
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*") 
                .allowCredentials(true); 
    }
}