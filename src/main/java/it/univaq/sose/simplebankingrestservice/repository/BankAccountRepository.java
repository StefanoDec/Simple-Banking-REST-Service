package it.univaq.sose.simplebankingrestservice.repository;

import it.univaq.sose.simplebankingrestservice.InsufficientFundsException;
import it.univaq.sose.simplebankingrestservice.NotFoundException;
import it.univaq.sose.simplebankingrestservice.domain.BankAccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BankAccountRepository {
    private static volatile BankAccountRepository instance;
    private final Map<Long, BankAccount> bankAccounts;
    private final ReentrantReadWriteLock lock;
    private long lastIndex;

    private BankAccountRepository() {
        if (instance != null) {
            throw new IllegalStateException("Already initialized.");
        }
        this.bankAccounts = new HashMap<>();
        this.lastIndex = -1;
        this.lock = new ReentrantReadWriteLock();
    }

    public static BankAccountRepository getInstance() {
        BankAccountRepository result = instance;
        if (result == null) {
            synchronized (BankAccountRepository.class) {
                result = instance;
                if (result == null) {
                    result = new BankAccountRepository();
                    instance = result;
                }
            }
        }
        return result;
    }

    private long getNextIndex() {
        lastIndex += 1;
        return lastIndex;
    }

    public List<BankAccount> findAll() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(bankAccounts.values());
        } finally {
            lock.readLock().unlock();
        }
    }

    public BankAccount findById(long id) throws NotFoundException {
        lock.readLock().lock();
        try {
//            return bankAccounts.get(id);
            BankAccount bankAccount = bankAccounts.get(id);
            if (bankAccount == null) {
                throw new NotFoundException("BankAccount with ID " + id + " not found.");
            }
            return bankAccount;
        } finally {
            lock.readLock().unlock();
        }
    }

    public long save(BankAccount bankAccount) {
        lock.writeLock().lock();
        try {
            long id = getNextIndex();
            bankAccount.setIdBankAccount(id);
            bankAccounts.put(id, bankAccount);
            return bankAccount.getIdBankAccount();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void delete(BankAccount bankAccount) {
        lock.writeLock().lock();
        try {
            bankAccounts.remove(bankAccount.getIdBankAccount());
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void addMoney(long idBankAccount, float amount) {
        lock.writeLock().lock();
        try {
            BankAccount account = bankAccounts.get(idBankAccount);
            if (account != null) {
                float newBalance = account.getMoney() + amount;
                account.setMoney(newBalance);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean removeMoney(long idBankAccount, float amount) throws NotFoundException, InsufficientFundsException {
        lock.writeLock().lock();
        try {
            BankAccount bankAccount = bankAccounts.get(idBankAccount);
            if (bankAccount == null) {
                throw new NotFoundException("Account with ID " + amount + " not found.");
            }
            if (bankAccount.getMoney() < amount) {
                throw new InsufficientFundsException("Insufficient funds for withdrawal.");
            }
            if (bankAccount.getMoney() >= amount) {
                float newBalance = bankAccount.getMoney() - amount;
                bankAccount.setMoney(newBalance);
                return true;
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }


    public long lastIdSave() {
        lock.readLock().lock();
        try {
            return bankAccounts.size() - 1;
        } finally {
            lock.readLock().unlock();
        }
    }

    public String generateNewBankAccountNumber() {
        lock.readLock().lock();
        try {
            if (!bankAccounts.isEmpty()) {

                String iban = bankAccounts.get((long) bankAccounts.size() - 1).getBankAccountNumber();
                int length = iban.length();
                String numericPart = iban.substring(length - 12, length);
                long number;

                try {
                    number = Long.parseLong(numericPart);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("L'IBAN fornito non è valido.");
                }

                number++; // incrementa il numero
                String newNumericPart = String.format("%012d", number); // mantiene gli zeri iniziali se necessario
                return iban.substring(0, length - 12) + newNumericPart;
            } else return "IT60X0542811101000000000001";
        } finally {
            lock.readLock().unlock();
        }
    }
}
