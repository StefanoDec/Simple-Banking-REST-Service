package it.univaq.sose.simplebankingrestservice.security;

import it.univaq.sose.simplebankingrestservice.domain.Role;
import org.apache.cxf.rs.security.jose.common.JoseType;
import org.apache.cxf.rs.security.jose.jwa.SignatureAlgorithm;
import org.apache.cxf.rs.security.jose.jws.*;
import org.apache.cxf.rs.security.jose.jwt.JwtClaims;
import org.apache.cxf.rs.security.jose.jwt.JwtToken;

public class JwtUtil {
    private static final String SECRET_KEY = "your_secret_key_here";

    private JwtUtil() {
        throw new IllegalStateException("JWT Utility class");
    }

    public static JwsSignatureProvider getJwsSignatureProvider() {
        return new HmacJwsSignatureProvider(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256);
    }

    public static JwsSignatureVerifier getJwsSignatureVerifier() {
        return new HmacJwsSignatureVerifier(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256);
    }

    public static String createJwtToken(String username, Role role) {
        JwsHeaders headers = new JwsHeaders(JoseType.JWT, SignatureAlgorithm.HS256);

        JwtClaims claims = new JwtClaims();
        claims.setSubject(username);// L'utente per cui il token è valido
        claims.setClaim("role", role.toString()); // Il ruolo in stringa
        claims.setIssuedAt(System.currentTimeMillis() / 1000);
        claims.setExpiryTime((System.currentTimeMillis() / 1000) + 3600);  // Token valido per 1 ora


        JwtToken token = new JwtToken(headers, claims);

        JwsJwtCompactProducer producer = new JwsJwtCompactProducer(token);
        return producer.signWith(getJwsSignatureProvider());
    }
}
