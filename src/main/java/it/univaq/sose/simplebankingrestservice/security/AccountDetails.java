package it.univaq.sose.simplebankingrestservice.security;


import it.univaq.sose.simplebankingrestservice.domain.Role;

public class AccountDetails {
    private String username;
    private Role role;

    public AccountDetails(String username, Role role) {
        this.username = username;
        this.role = role;
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
}
