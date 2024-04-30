package it.univaq.sose.simplebankingrestservice.security;

import it.univaq.sose.simplebankingrestservice.domain.Role;
import org.apache.cxf.rs.security.jose.jwa.SignatureAlgorithm;
import org.apache.cxf.rs.security.jose.jws.*;
import org.apache.cxf.rs.security.jose.jwt.JwtClaims;
import org.apache.cxf.rs.security.jose.jwt.JwtToken;

public class JwtUtil {
    private static final String SECRET_KEY = "your_secret_key_here";

    public static JwsSignatureProvider getJwsSignatureProvider() {
        return new HmacJwsSignatureProvider(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256);
    }

    public static JwsSignatureVerifier getJwsSignatureVerifier() {
        return new HmacJwsSignatureVerifier(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256);
    }

    public static String createJwtToken(String username, Role role) {
        // Creare l'header del JWT
        JwsHeaders headers = new JwsHeaders(SignatureAlgorithm.HS256);

        // Creare le claims del JWT
        JwtClaims claims = new JwtClaims();
        claims.setSubject(username);  // L'utente per cui il token è valido
        claims.setClaim("role", role.toString());
        claims.setIssuedAt(System.currentTimeMillis() / 1000);
        claims.setExpiryTime((System.currentTimeMillis() / 1000) + 3600);  // Token valido per 1 ora

        // Creare il token JWT
        JwtToken token = new JwtToken(headers, claims);

        // Produrre il JWT firmato
        JwsJwtCompactProducer producer = new JwsJwtCompactProducer(token);
        return producer.signWith(getJwsSignatureProvider());
    }
}
