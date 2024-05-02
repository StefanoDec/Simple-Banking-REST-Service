package it.univaq.sose.simplebankingrestservice.security;

import it.univaq.sose.simplebankingrestservice.domain.Role;
import org.apache.cxf.rs.security.jose.common.JoseType;
import org.apache.cxf.rs.security.jose.jwa.SignatureAlgorithm;
import org.apache.cxf.rs.security.jose.jws.*;
import org.apache.cxf.rs.security.jose.jwt.JwtClaims;
import org.apache.cxf.rs.security.jose.jwt.JwtToken;

public class JwtUtil {
    private static final String SECRET_KEY = "3f8bRb6!fc84c8#8^aB5Df99*45d&Ef2";

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
        claims.setSubject(username);// The user for whom the token is valid
        claims.setClaim("role", role.toString()); // The role in string
        claims.setIssuedAt(System.currentTimeMillis() / 1000);
        claims.setExpiryTime((System.currentTimeMillis() / 1000) + 3600);  // Token valid for 1 H


        JwtToken token = new JwtToken(headers, claims);

        JwsJwtCompactProducer producer = new JwsJwtCompactProducer(token);
        return producer.signWith(getJwsSignatureProvider());
    }
}
