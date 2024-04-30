package it.univaq.sose.simplebankingrestservice.security;

import org.apache.cxf.rs.security.jose.common.JoseException;
import org.apache.cxf.rs.security.jose.jaxrs.JwtAuthenticationFilter;
import org.apache.cxf.rs.security.jose.jaxrs.JwtTokenSecurityContext;
import org.apache.cxf.rs.security.jose.jwt.JwtToken;
import org.apache.cxf.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import java.io.IOException;

public class BankJwtAuthenticationFilter extends JwtAuthenticationFilter {
    private static final Logger LOG = LoggerFactory.getLogger(BankJwtAuthenticationFilter.class);
    private static final String EXPECTED_AUTH_SCHEME = "Bearer";

    public BankJwtAuthenticationFilter() {
        super();
        this.setExpectedAuthScheme(EXPECTED_AUTH_SCHEME);
        this.setJwsVerifier(JwtUtil.getJwsSignatureVerifier());
        this.setRoleClaim("role");
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (skipAuthentication(requestContext.getUriInfo().getPath())) {
            return;
        }
        try {
            super.filter(requestContext);
        } catch (JoseException e) {
            String type = requestContext.getHeaderString("Accept");
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\": \"" + e.getMessage() + "\"}").type(type).build());
        }
    }

    @Override
    protected SecurityContext configureSecurityContext(JwtToken jwt) {
        if (jwt.getClaims().getSubject() != null && jwt.getClaims().getClaim("role") != null) {
            return new JwtTokenSecurityContext(jwt, super.getRoleClaim());
        }
        return null;
    }


    private boolean skipAuthentication(String path) {
        return path.endsWith("login") || path.endsWith("openapi.json") || path.endsWith("api-docs") || path.startsWith("swagger") || path.startsWith("favicon");
    }
}
