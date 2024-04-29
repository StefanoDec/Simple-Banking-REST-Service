package it.univaq.sose.simplebankingrestservice.filters;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Provider
public class RequestLoggingFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Log method and URI
        String method = requestContext.getMethod();
        String uri = requestContext.getUriInfo().getRequestUri().toString();
        System.out.println("Request Method: " + method);
        System.out.println("Request URI: " + uri);

        // Log headers
        MultivaluedMap<String, String> headers = requestContext.getHeaders();
        System.out.println("Request Headers: ");
        headers.forEach((key, value) -> System.out.println(key + ": " + value));

        // Log body
        InputStream in = requestContext.getEntityStream();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > -1) {
            out.write(buf, 0, len);
        }
        byte[] requestEntity = out.toByteArray();
        String body = new String(requestEntity, "UTF-8");
        System.out.println("Request body: " + body);

        // Reset the InputStream so that it can be read again by the actual processing/filter.
        requestContext.setEntityStream(new ByteArrayInputStream(requestEntity));
    }
}
