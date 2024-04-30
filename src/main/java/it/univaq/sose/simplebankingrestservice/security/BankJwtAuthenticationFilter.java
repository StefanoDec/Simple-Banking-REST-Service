package it.univaq.sose.simplebankingrestservice.security;

import org.apache.cxf.interceptor.security.AccessDeniedException;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageUtils;
import org.apache.cxf.rs.security.jose.common.JoseConstants;
import org.apache.cxf.rs.security.jose.jaxrs.JwtAuthenticationFilter;
import org.apache.cxf.rs.security.jose.jaxrs.JwtTokenSecurityContext;
import org.apache.cxf.rs.security.jose.jwa.SignatureAlgorithm;
import org.apache.cxf.rs.security.jose.jwt.JwtException;
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
        LOG.debug("Role in filtro {}", this.getRoleClaim());
        String encodedJwtToken = getEncodedJwtToken(requestContext);
        try {
            JwtToken token = super.getJwtToken(encodedJwtToken);
            SecurityContext securityContext = configureSecurityContext(token);
            if (securityContext != null) {
                JAXRSUtils.getCurrentMessage().put(SecurityContext.class, securityContext);
            }
        } catch (JwtException e) {
            String type = requestContext.getHeaderString("Accept");
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).type(type).build());
        }

    }

    @Override
    protected SecurityContext configureSecurityContext(JwtToken jwt) {
        Message m = JAXRSUtils.getCurrentMessage();
        boolean enableUnsignedJwt =
                MessageUtils.getContextualBoolean(m, JoseConstants.ENABLE_UNSIGNED_JWT_PRINCIPAL, true);

        // The token must be signed/verified with a public key to set up the security context,
        // unless we directly configure otherwise
        if (jwt.getClaims().getSubject() != null
                && (isVerifiedWithAPublicKey(jwt) || enableUnsignedJwt)) {
            return new JwtTokenSecurityContext(jwt, super.getRoleClaim());
        }
        return null;
    }

    private boolean isVerifiedWithAPublicKey(JwtToken jwt) {
        if (isJwsRequired()) {
            String alg = (String) jwt.getJwsHeader(JoseConstants.HEADER_ALGORITHM);
            SignatureAlgorithm sigAlg = SignatureAlgorithm.getAlgorithm(alg);
            LOG.debug("isVerifiedWithAPublicKey {}", SignatureAlgorithm.isPublicKeyAlgorithm(sigAlg));
            return SignatureAlgorithm.isPublicKeyAlgorithm(sigAlg);
        }
        LOG.debug("isVerifiedWithAPublicKey {}", false);
        return false;
    }


    private boolean skipAuthentication(String path) {
        return path.endsWith("login") || path.endsWith("openapi.json") || path.endsWith("api-docs") || path.startsWith("swagger") || path.startsWith("favicon");
    }
}
