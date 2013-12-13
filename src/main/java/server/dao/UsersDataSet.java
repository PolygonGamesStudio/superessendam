package server.dao;

public class UsersDataSet {
    private long id;
    private String name;
    private String password;

    public UsersDataSet(long id, String name, String password){
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
    public long getId() {
        return id;
    }
}