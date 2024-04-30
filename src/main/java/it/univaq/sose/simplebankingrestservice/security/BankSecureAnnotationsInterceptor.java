package it.univaq.sose.simplebankingrestservice.security;

import it.univaq.sose.simplebankingrestservice.webapi.BankRestApiImpl;
import org.apache.cxf.interceptor.security.SecureAnnotationsInterceptor;

public class BankSecureAnnotationsInterceptor extends SecureAnnotationsInterceptor {

    public BankSecureAnnotationsInterceptor() {
        super();
        this.setSecuredObject(new BankRestApiImpl());
    }
}
