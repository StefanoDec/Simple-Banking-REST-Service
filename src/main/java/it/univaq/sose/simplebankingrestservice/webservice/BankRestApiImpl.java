package it.univaq.sose.simplebankingrestservice.webservice;

import it.univaq.sose.simplebankingrestservice.repository.AccountRepository;
import it.univaq.sose.simplebankingrestservice.repository.BankAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BankRestApiImpl implements BankRestApi {
    private static final Logger LOG = LoggerFactory.getLogger(BankRestApiImpl.class);
    private AccountRepository accountRepository = AccountRepository.getInstance();
    private BankAccountRepository bankAccountRepository = BankAccountRepository.getInstance();

    @Override
    public Integer getBank() {
        return 1;
    }
}
