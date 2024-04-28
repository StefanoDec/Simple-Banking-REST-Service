package it.univaq.sose.simplebankingrestservice.dto;

import it.univaq.sose.simplebankingrestservice.domain.BankAccount;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccountAndBankAccount", propOrder = {
        "idAccount",
        "name",
        "surname",
        "username",
        "bankAccount"
})
public class AccountAndBankAccount {
    @XmlElement(required = true)
    private long idAccount;

    @XmlElement(required = true)
    private String name;

    @XmlElement(required = true)
    private String surname;

    @XmlElement(required = true)
    private String username;

    private BankAccount bankAccount;

    public AccountAndBankAccount() {
    }

    public AccountAndBankAccount(long idAccount, String name, String surname, String username, BankAccount bankAccount) {
        this.idAccount = idAccount;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.bankAccount = bankAccount;
    }

    public long getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(long idAccount) {
        this.idAccount = idAccount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountAndBankAccount)) return false;

        AccountAndBankAccount that = (AccountAndBankAccount) o;
        return getIdAccount() == that.getIdAccount();
    }

    @Override
    public int hashCode() {
        return Long.hashCode(getIdAccount());
    }

    @Override
    public String toString() {
        return "AccountAndBankAccount{" +
                "idProfile=" + idAccount +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", username='" + username + '\'' +
                ", bankAccount='" + bankAccount + '\'' +
                '}';
    }
}
