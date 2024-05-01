package it.univaq.sose.simplebankingrestservice.security;

import it.univaq.sose.simplebankingrestservice.dto.ErrorResponse;
import org.apache.cxf.interceptor.security.AbstractAuthorizingInInterceptor;
import org.apache.cxf.interceptor.security.AccessDeniedException;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static it.univaq.sose.simplebankingrestservice.util.Utils.getFirstMediaType;

@Priority(Priorities.AUTHORIZATION)
public class BankSimpleAuthorizingFilter implements ContainerRequestFilter {
    private AbstractAuthorizingInInterceptor interceptor;

    public BankSimpleAuthorizingFilter() {
        setInterceptor(new BankSecureAnnotationsInterceptor());
    }

    @Override
    public void filter(ContainerRequestContext context) {
        try {
            interceptor.handleMessage(JAXRSUtils.getCurrentMessage());
        } catch (AccessDeniedException ex) {
            String acceptHeader = context.getHeaderString("Accept");
            MediaType type = getFirstMediaType(acceptHeader);
            context.abortWith(Response.status(Response.Status.FORBIDDEN).entity(new ErrorResponse(ex.getMessage())).type(type).build());
        }
    }

    public void setInterceptor(AbstractAuthorizingInInterceptor in) {
        interceptor = in;
    }
}
