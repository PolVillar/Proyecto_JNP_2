package model;

public class UserInMemory {
    private static UserInMemory INSTANCE;
    private User user;

    private UserInMemory(){

    }

    public static UserInMemory getInstance(){
        if (INSTANCE == null){
            INSTANCE = new UserInMemory();
        }
        return INSTANCE;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
