package it.univaq.sose.simplebankingrestservice;

import org.apache.cxf.interceptor.security.SecureAnnotationsInterceptor;
import org.apache.cxf.jaxrs.security.ClaimsAuthorizingFilter;
import org.apache.cxf.jaxrs.security.SimpleAuthorizingFilter;
import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.apache.cxf.jaxrs.validation.JAXRSBeanValidationInInterceptor;
import org.apache.cxf.rs.security.jose.jaxrs.JwtAuthenticationClientFilter;
import org.apache.cxf.rs.security.jose.jaxrs.JwtAuthenticationFilter;
import org.apache.cxf.rs.security.jose.jaxrs.JwtTokenSecurityContext;

public class SimpleBankingRESTServiceApplication extends CXFNonSpringJaxrsServlet {
    private static final long serialVersionUID = 8409832783964290070L;

    public void test(){
        new ClaimsAuthorizingFilter();
        JwtAuthenticationFilter a = new JwtAuthenticationFilter();
        SimpleAuthorizingFilter b = new SimpleAuthorizingFilter();
        SecureAnnotationsInterceptor c = new SecureAnnotationsInterceptor();

    }
}
