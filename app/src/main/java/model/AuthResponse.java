package model;

public class AuthResponse {
    private String username;
    private String message;
    private String jwt;
    private boolean status;

    public AuthResponse(String username, String message, String jwt, boolean status) {
        this.username = username;
        this.message = message;
        this.jwt = jwt;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
