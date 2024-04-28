package it.univaq.sose.simplebankingrestservice.webapi;

public class InsufficientFundsException extends Exception {
    private static final long serialVersionUID = 7692673956797365627L;

    public InsufficientFundsException(String message) {
        super(message);
    }
}
