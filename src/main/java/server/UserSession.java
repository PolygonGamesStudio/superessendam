package server;

public class UserSession {
    private Address address;

    private String login;
    private String password;
    private String sessionId;
    private Long userId;

    public UserSession(AddressService addressService, String login, String password, String sessionId) {
        this.address = addressService.getAddress();
        this.login = login;
        this.password = password;
        this.sessionId = sessionId;
    }

    public Address getAddress() {
        return address;
    }

    public String getLogin() {
        return login;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean correctPassword(String externalPassword) {
        return password.equals(externalPassword);
    }
}
