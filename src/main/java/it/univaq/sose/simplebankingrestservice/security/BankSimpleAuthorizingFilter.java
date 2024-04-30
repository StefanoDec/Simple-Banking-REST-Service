package it.univaq.sose.simplebankingrestservice.security;

import org.apache.cxf.jaxrs.security.SimpleAuthorizingFilter;

public class BankSimpleAuthorizingFilter extends SimpleAuthorizingFilter {
    public BankSimpleAuthorizingFilter() {
        super();
        this.setInterceptor(new BankSecureAnnotationsInterceptor());
    }
}
