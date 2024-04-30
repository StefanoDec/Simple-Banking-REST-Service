package it.univaq.sose.simplebankingrestservice.webapi;

import it.univaq.sose.simplebankingrestservice.domain.Account;
import it.univaq.sose.simplebankingrestservice.domain.BankAccount;
import it.univaq.sose.simplebankingrestservice.domain.Role;
import it.univaq.sose.simplebankingrestservice.dto.*;
import it.univaq.sose.simplebankingrestservice.repository.AccountRepository;
import it.univaq.sose.simplebankingrestservice.repository.BankAccountRepository;
import it.univaq.sose.simplebankingrestservice.security.AccountDetails;
import it.univaq.sose.simplebankingrestservice.security.JwtUtil;
import it.univaq.sose.simplebankingrestservice.util.AuthenticationUtils;
import org.apache.cxf.interceptor.security.AuthenticationException;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BankRestApiImpl implements BankRestApi {
    private static final Logger LOG = LoggerFactory.getLogger(BankRestApiImpl.class);
    private AccountRepository accountRepository = AccountRepository.getInstance();
    private BankAccountRepository bankAccountRepository = BankAccountRepository.getInstance();

    @Context
    MessageContext jaxrsContext;


    @Override
    public List<AccountResponse> getAllServiceAccounts() {
        return accountRepository.findAll().stream()
                .filter(account -> account.getRole() == Role.ADMIN || account.getRole() == Role.BANKER)
                .map(account -> new AccountResponse(account.getIdAccount(), account.getName(), account.getSurname(), account.getUsername(), account.getRole()))
                .collect(Collectors.toList());
    }

    @Override
    public AccountResponse saveAdminAccount(AccountRequest request) {
        Account account = new Account(0, request.getName(), request.getSurname(), request.getUsername(), request.getPassword(), Role.ADMIN);
        long idAccount = accountRepository.save(account);
        LOG.info("Risposta saveAdminAccount");
        AccountResponse accountResponse = new AccountResponse(idAccount, account.getName(), account.getSurname(), account.getUsername(), account.getRole());
        LOG.info("{}", accountResponse);
        return accountResponse;
    }

    @Override
    public AccountResponse saveBankerAccount(AccountRequest request) {
        Account account = new Account(0, request.getName(), request.getSurname(), request.getUsername(), request.getPassword(), Role.BANKER);
        long idAccount = accountRepository.save(account);
        LOG.info("Risposta saveBankerAccount");
        AccountResponse accountResponse = new AccountResponse(idAccount, account.getName(), account.getSurname(), account.getUsername(), account.getRole());
        LOG.info("{}", accountResponse);
        return accountResponse;
    }

    @Override
    public List<AccountAndBankAccount> getAllAccountsAndBankAccounts() {
        List<AccountAndBankAccount> accountsAndBankAccounts = new LinkedList<>();

        List<Account> accounts = accountRepository.findAll();
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();

        accounts.forEach(account -> {
            Optional<BankAccount> matchingBankAccount = bankAccounts.stream()
                    .filter(bankAccount -> account.getIdBankAccount() != null && bankAccount.getIdBankAccount() == account.getIdBankAccount())
                    .findFirst();

            matchingBankAccount.ifPresent(bankAccount -> accountsAndBankAccounts.add(new AccountAndBankAccount(
                    account.getIdAccount(),
                    account.getName(),
                    account.getSurname(),
                    account.getUsername(),
                    bankAccount
            )));
        });
        LOG.info("Risposta getAllAccountsAndBankAccounts");
        LOG.info("{}", accountsAndBankAccounts);
        return accountsAndBankAccounts;
    }

    @Override
    public AccountAndBankAccount getAccountAndBankAccount(long id) throws NotFoundException {
        AccountDetails authenticationDetails = AuthenticationUtils.getAuthenticationDetails(jaxrsContext);
        Account account = accountRepository.findById(id);
        if (authenticationDetails.getRole().equals(Role.CUSTOMER) && !account.getUsername().equals(authenticationDetails.getUsername())) {
            throw new AuthenticationException("You are not authorised to make this request");
        }
        BankAccount bankAccount = bankAccountRepository.findById(account.getIdBankAccount());
        LOG.info("Risposta getAccountAndBankAccount");
        AccountAndBankAccount accountAndBankAccount = new AccountAndBankAccount(account.getIdAccount(), account.getName(), account.getSurname(), account.getUsername(), bankAccount);
        LOG.info("{}", accountAndBankAccount);
        return accountAndBankAccount;
    }

    @Override
    public AccountAndBankAccount saveAccountAndBankAccount(OpenBankAccountRequest request) {
        String bankAccountNumber = bankAccountRepository.generateNewBankAccountNumber();
        Account account = new Account(0, request.getName(), request.getSurname(), request.getUsername(), request.getPassword(), Role.CUSTOMER);
        long idAccount = accountRepository.save(account);
        BankAccount bankAccount = new BankAccount(0, bankAccountNumber, request.getMoney(), idAccount);
        bankAccountRepository.save(bankAccount);
        account.setIdBankAccount(bankAccount);
        idAccount = accountRepository.updateIdBankAccount(account);
        LOG.info("Risposta saveAccountAndBankAccount");
        AccountAndBankAccount andBankAccount = new AccountAndBankAccount(idAccount, account.getName(), account.getSurname(), account.getUsername(), bankAccount);
        LOG.info("{}", andBankAccount);
        return andBankAccount;
    }

    @Override
    public boolean deleteAccount(long id) throws NotFoundException {
        Account account = accountRepository.findById(id);
        accountRepository.delete(account);
        if (account.getRole().equals(Role.CUSTOMER)) {
            BankAccount bankAccount = bankAccountRepository.findById(account.getIdBankAccount());
            bankAccountRepository.delete(bankAccount);
        }
        return true;
    }

    @Override
    public AccountAndBankAccount depositMoneyInBankAccount(MoneyTransfer moneyTransfer, long id) throws NotFoundException {
        AccountDetails authenticationDetails = AuthenticationUtils.getAuthenticationDetails(jaxrsContext);
        BankAccount bankAccount = bankAccountRepository.findById(moneyTransfer.getIdBankAccount());
        Account account = accountRepository.findById(bankAccount.getAccount());
        if (authenticationDetails.getRole().equals(Role.CUSTOMER) && !account.getUsername().equals(authenticationDetails.getUsername())) {
            throw new AuthenticationException("You are not authorised to make this request");
        }
        bankAccountRepository.addMoney(moneyTransfer.getIdBankAccount(), moneyTransfer.getAmount());
        bankAccount = bankAccountRepository.findById(moneyTransfer.getIdBankAccount());
        AccountAndBankAccount accountAndBankAccount = new AccountAndBankAccount(account.getIdAccount(), account.getName(), account.getSurname(), account.getUsername(), bankAccount);
        LOG.info("Risposta depositMoneyInBankAccount");
        LOG.info("{}", accountAndBankAccount);
        return accountAndBankAccount;
    }

    @Override
    public AccountAndBankAccount withdrawMoneyInBankAccount(MoneyTransfer moneyTransfer, long id) throws NotFoundException, InsufficientFundsException {
        AccountDetails authenticationDetails = AuthenticationUtils.getAuthenticationDetails(jaxrsContext);
        BankAccount bankAccount = bankAccountRepository.findById(moneyTransfer.getIdBankAccount());
        Account account = accountRepository.findById(bankAccount.getAccount());
        if (authenticationDetails.getRole().equals(Role.CUSTOMER) && !account.getUsername().equals(authenticationDetails.getUsername())) {
            throw new AuthenticationException("You are not authorised to make this request");
        }
        bankAccountRepository.removeMoney(moneyTransfer.getIdBankAccount(), moneyTransfer.getAmount());
        bankAccount = bankAccountRepository.findById(moneyTransfer.getIdBankAccount());
        AccountAndBankAccount accountAndBankAccount = new AccountAndBankAccount(account.getIdAccount(), account.getName(), account.getSurname(), account.getUsername(), bankAccount);
        LOG.info("Risposta withdrawMoneyInBankAccount");
        LOG.info("{}", accountAndBankAccount);
        return accountAndBankAccount;
    }

    @Override
    public Response login(UserCredentials credentials) {
        Account account;
        try {
            account = accountRepository.findByUsername(credentials.getUsername());
        } catch (NotFoundException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\": \"Invalid username or password\"}").build();
        }
        if (account.getPassword().equals(credentials.getPassword())) {
            String token = JwtUtil.createJwtToken(credentials.getUsername(), Role.ADMIN);
            return Response.ok(new TokenResponse(token)).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\": \"Invalid username or password\"}").build();
    }

}
