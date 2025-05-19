package com.tfg.egm.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import java.util.Date;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

/**
 * Utilidad para la generación, validación y extracción de información de tokens JWT.
 * Permite crear tokens, extraer usuario y rol, y validar la autenticidad y vigencia del token.
 */
@Component
public class JwtTokenUtil {

    private static final String SECRET_KEY = "EGM_TFG_Clave_Secreta_Indescifrable";  
    private static final long EXPIRATION_TIME = 86400000; 

    /**
     * Crea una clave de firma a partir de la clave secreta.
     * @return clave de firma HMAC
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /**
     * Genera un token JWT para un usuario y rol dados.
     * @param username nombre de usuario
     * @param rol rol del usuario
     * @return token JWT generado
     */
    public String generateToken(String username, String rol) {
        return Jwts.builder()
                .setSubject(username)
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey()) 
                .compact();
    }

    /**
     * Extrae el nombre de usuario del token JWT.
     * @param token token JWT
     * @return nombre de usuario
     */
    public String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder()  
                .setSigningKey(getSigningKey())  
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    /**
     * Extrae el rol del usuario del token JWT.
     * @param token token JWT
     * @return rol del usuario
     */
    public String extractRole(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())  
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("rol", String.class); 
    }

    /**
     * Valida el token comprobando usuario, rol y expiración.
     * @param token token JWT
     * @param username nombre de usuario esperado
     * @param rol rol esperado
     * @return true si el token es válido, false si no
     */
    public Boolean validateToken(String token, String username, String rol) {
        return (username.equals(extractUsername(token)) && rol.equals(extractRole(token)) && !isTokenExpired(token));
    }

    /**
     * Verifica si el token está expirado.
     * @param token token JWT
     * @return true si está expirado, false si no
     */
    private Boolean isTokenExpired(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration().before(new Date());
    }
}