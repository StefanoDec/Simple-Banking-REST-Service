package it.univaq.sose.simplebankingrestservice.dto;

import it.univaq.sose.simplebankingrestservice.domain.Role;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccountResponse", propOrder = {
        "idAccount",
        "name",
        "surname",
        "username",
        "role"
})
public class AccountResponse {
    @XmlElement(required = true)
    private long idAccount;

    @XmlElement(required = true)
    private String name;

    @XmlElement(required = true)
    private String surname;

    @XmlElement(required = true)
    private String username;

    @XmlElement(required = true)
    private Role role;


    public AccountResponse() {
    }

    public AccountResponse(long idAccount, String name, String surname, String username, Role role) {
        this.idAccount = idAccount;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.role = role;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public long getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(long idAccount) {
        this.idAccount = idAccount;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountResponse)) return false;

        AccountResponse that = (AccountResponse) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getSurname(), that.getSurname());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getName());
        result = 31 * result + Objects.hashCode(getSurname());
        result = 31 * result + Objects.hashCode(getUsername());
        return result;
    }

    @Override
    public String toString() {
        return "OpenBankAccountRequest{" +
                "idAccount='" + idAccount + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

}