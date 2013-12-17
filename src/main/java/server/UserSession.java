package server;

public class UserSession {
    private Address addressFE;  // Frontend
    private Address addressAS;  // Account Service
    private Address addressGM;  // Game Mechanics


    private String login;
    private String sessionId;
    private Long userId;
    private boolean isAuthResponseFromServer;

    //    public UserSession(Address addressFE, String login, String password, String sessionId) {
    public UserSession(Address addressFE, Address addressGM, Address addressAS, String login, String sessionId) {

        this.addressFE = addressFE;
        this.addressGM = addressGM;
        this.addressAS = addressAS;
        this.login = login;
        this.sessionId = sessionId;
        this.isAuthResponseFromServer = false;
    }

    public Address getAddressFE() {
        return addressFE;
    }

    public Address getAddressGM() {
        return addressGM;
    }

    public Address getAddressAS() {
        return addressAS;
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
