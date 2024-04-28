package it.univaq.sose.simplebankingrestservice;

import it.univaq.sose.simplebankingrestservice.repository.AccountRepository;
import it.univaq.sose.simplebankingrestservice.repository.BankAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BankRestApiImpl implements BankRestApi {
    private static final Logger logger = LoggerFactory.getLogger(BankRestApiImpl.class);
    private AccountRepository accountRepository = AccountRepository.getInstance();
    private BankAccountRepository bankAccountRepository = BankAccountRepository.getInstance();

    @Override
    public int getBank() {
        return 1;
    }
}
