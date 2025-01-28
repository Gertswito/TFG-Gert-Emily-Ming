package com.tfg.egm.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import java.util.Date;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

@Component
public class JwtTokenUtil {

    private static final String SECRET_KEY = "EGM_TFG_Clave_Secreta_Indescifrable";  
    private static final long EXPIRATION_TIME = 86400000; 

    // Crear una clave a partir de la clave secreta
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Generar el JWT
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey()) 
                .compact();
    }

    // Extraer el nombre de usuario del JWT
    public String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder()  
                .setSigningKey(getSigningKey())  
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // Validar el token
    public Boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }

    // Verificar si el token está expirado
    private Boolean isTokenExpired(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration().before(new Date());
    }
}