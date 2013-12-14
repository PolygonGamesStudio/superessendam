package server;

public class UserSession {
    private Address addressFE;

    public Address getAddressGM() {
        return addressGM;
    }

    private Address addressGM;

    private String login;
    //    private String password;
    private String sessionId;
    private Long userId;
    private boolean isAuthResponseFromServer;

    //    public UserSession(Address addressFE, String login, String password, String sessionId) {
    public UserSession(Address addressFE, Address addressGM, String login, String sessionId) {

        this.addressFE = addressFE;
        this.addressGM = addressGM;
        this.login = login;
//        this.password = password;
        this.sessionId = sessionId;
        this.isAuthResponseFromServer = false;
    }

    public Address getAddressFE() {
        return addressFE;
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
}
