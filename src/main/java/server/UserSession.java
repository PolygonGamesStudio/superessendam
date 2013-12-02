package server;

public class UserSession {
    private Address address;

    private String login;
    //    private String password;
    private String sessionId;
    private Long userId;
    private boolean isAuthResponseFromServer;

    //    public UserSession(Address address, String login, String password, String sessionId) {
    public UserSession(Address address, String login, String sessionId) {
        this.address = address;
        this.login = login;
//        this.password = password;
        this.sessionId = sessionId;
        this.isAuthResponseFromServer = false;
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

    public void setAuthResponseFromServer() {
        isAuthResponseFromServer = true;
    }

    public boolean gotAuthResponse() {
        return isAuthResponseFromServer;
    }

//    public void logout() {
//        this.login = null;
//        this.password = null;
//        this.userId = null;
//        this.isAuthResponseFromServer = false;
//    }
}
