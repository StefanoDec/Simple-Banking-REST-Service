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

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BankRestApiImpl implements BankRestApi {
    private static final Logger LOG = LoggerFactory.getLogger(BankRestApiImpl.class);
    private final AccountRepository accountRepository = AccountRepository.getInstance();
    private final BankAccountRepository bankAccountRepository = BankAccountRepository.getInstance();

    @Context
    MessageContext jaxrsContext;


    @Override
    public void getAllServiceAccounts(AsyncResponse asyncResponse) {
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                List<AccountResponse> accountResponses = accountRepository.findAll().stream()
                        .filter(account -> account.getRole() == Role.ADMIN || account.getRole() == Role.BANKER)
                        .map(account -> new AccountResponse(account.getIdAccount(), account.getName(), account.getSurname(), account.getUsername(), account.getRole()))
                        .collect(Collectors.toList());
                Response response = Response.ok(accountResponses).build();
                asyncResponse.resume(response);
            } catch (InterruptedException e) {
                Response response = Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                /* Clean up whatever needs to be handled before interrupting  */
                Thread.currentThread().interrupt();
            }
        }).start();
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
        AccountResponse accountResponse = new AccountResponse(idAccount, account.getName(), account.getSurname(), account.getUsername(), account.getRole());
        return accountResponse;
    }

    @Override
    public void getAllAccountsAndBankAccounts(AsyncResponse asyncResponse) {
        new Thread(() -> {
            try {
                Thread.sleep(2000);
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
                Response response = Response.ok(accountsAndBankAccounts).build();
                asyncResponse.resume(response);
            } catch (InterruptedException e) {
                Response response = Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                /* Clean up whatever needs to be handled before interrupting  */
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    @Override
    public void getAccountAndBankAccount(long id, AsyncResponse asyncResponse) {
        String username = jaxrsContext.getSecurityContext().getUserPrincipal().getName();
        new Thread(() -> {
            try {

                Thread.sleep(2000);
                AccountDetails authenticationDetails = AuthenticationUtils.getAuthenticationDetails(username);
                Account account = accountRepository.findById(id);
                if (authenticationDetails.getRole().equals(Role.CUSTOMER) && !account.getUsername().equals(authenticationDetails.getUsername())) {
                    throw new AuthenticationException("You are not authorised to make this request");
                }
                BankAccount bankAccount = bankAccountRepository.findById(account.getIdBankAccount());
                Response response = Response.ok(new AccountAndBankAccount(account.getIdAccount(), account.getName(), account.getSurname(), account.getUsername(), bankAccount)).build();
                asyncResponse.resume(response);
            } catch (InterruptedException e) {
                Response response = Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                /* Clean up whatever needs to be handled before interrupting  */
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                Response response = Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
            }
        }).start();

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
        AccountAndBankAccount andBankAccount = new AccountAndBankAccount(idAccount, account.getName(), account.getSurname(), account.getUsername(), bankAccount);
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
    public void depositMoneyInBankAccount(MoneyTransfer moneyTransfer, long id, AsyncResponse asyncResponse) {
        String username = jaxrsContext.getSecurityContext().getUserPrincipal().getName();
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                AccountDetails authenticationDetails = AuthenticationUtils.getAuthenticationDetails(username);
                BankAccount bankAccount = bankAccountRepository.findById(moneyTransfer.getIdBankAccount());
                Account account = accountRepository.findById(bankAccount.getAccount());
                if (authenticationDetails.getRole().equals(Role.CUSTOMER) && !account.getUsername().equals(authenticationDetails.getUsername())) {
                    throw new AuthenticationException("You are not authorised to make this request");
                }
                bankAccountRepository.addMoney(moneyTransfer.getIdBankAccount(), moneyTransfer.getAmount());
                bankAccount = bankAccountRepository.findById(moneyTransfer.getIdBankAccount());
                AccountAndBankAccount accountAndBankAccount = new AccountAndBankAccount(account.getIdAccount(), account.getName(), account.getSurname(), account.getUsername(), bankAccount);
                Response response = Response.ok(accountAndBankAccount).build();
                asyncResponse.resume(response);
            } catch (InterruptedException e) {
                Response response = Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
                /* Clean up whatever needs to be handled before interrupting  */
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                Response response = Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
                asyncResponse.resume(response);
            }
        }).start();

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
        return accountAndBankAccount;
    }

    @Override
    public Response login(UserCredentials credentials) {
        Account account;
        try {
            account = accountRepository.findByUsername(credentials.getUsername());
        } catch (NotFoundException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ErrorResponse("Invalid username or password")).build();
        }
        if (account.getPassword().equals(credentials.getPassword())) {
            String token = JwtUtil.createJwtToken(credentials.getUsername(), account.getRole());
            return Response.ok(new TokenResponse(token)).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity(new ErrorResponse("Invalid username or password")).build();
    }

}
