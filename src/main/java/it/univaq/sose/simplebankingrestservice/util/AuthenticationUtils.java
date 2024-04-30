package it.univaq.sose.simplebankingrestservice.util;

import it.univaq.sose.simplebankingrestservice.domain.Account;
import it.univaq.sose.simplebankingrestservice.repository.AccountRepository;
import it.univaq.sose.simplebankingrestservice.security.AccountDetails;
import it.univaq.sose.simplebankingrestservice.webapi.NotFoundException;
import org.apache.cxf.interceptor.security.AuthenticationException;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class AuthenticationUtils {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationUtils.class);

    private AuthenticationUtils() {
    }

    public static AccountDetails getAuthenticationDetails(MessageContext jaxrsContext) {
        AccountRepository accountRepository = AccountRepository.getInstance();
        if (jaxrsContext.getSecurityContext() != null && jaxrsContext.getSecurityContext().getUserPrincipal() != null) {
            try {
                Account account = accountRepository.findByUsername(jaxrsContext.getSecurityContext().getUserPrincipal().getName());
                return new AccountDetails(jaxrsContext.getSecurityContext().getUserPrincipal().getName(), account.getRole());
            } catch (NotFoundException e) {
                throw new AuthenticationException("You are not authorised to make this request");
            }
        }
        throw new AuthenticationException("You are not authorised to make this request");
    }
}
