package com.tfg.egm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad para la aplicación.
 * Permite todas las peticiones y desactiva CSRF.
 */
@Configuration
public class SecurityConfig {

    /**
     * Configura la cadena de filtros de seguridad.
     * Actualmente permite todas las peticiones y desactiva CSRF.
     *
     * @param http objeto HttpSecurity
     * @return SecurityFilterChain configurada
     * @throws Exception en caso de error de configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/**").permitAll() 
            );
        return http.build();
    }

    /**
     * Bean para codificar contraseñas usando BCrypt.
     *
     * @return instancia de BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}