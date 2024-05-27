package model;

public class UserJwtInMemory {
    private static UserJwtInMemory INSTANCE;
    private User user;
    private String token;

    private UserJwtInMemory(){

    }

    public static UserJwtInMemory getInstance(){
        if (INSTANCE == null){
            INSTANCE = new UserJwtInMemory();
        }
        return INSTANCE;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public void cleanse(){
        this.user = null;
        this.token = null;
    }
}
