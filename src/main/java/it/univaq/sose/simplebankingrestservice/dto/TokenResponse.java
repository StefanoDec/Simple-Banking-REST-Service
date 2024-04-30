package it.univaq.sose.simplebankingrestservice.dto;
import java.util.StringJoiner;
public class TokenResponse {
    private String token;
    public TokenResponse() {
    }
    public TokenResponse(String token) {
        this.token = token;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    @Override
    public String toString() {
        return new StringJoiner(", ", TokenResponse.class.getSimpleName() + "[", "]")
                .add("token='" + token + "'")
                .toString();
    }
}