package server;

public class UserSession {
    private Address address;

    private String name;
    private String sessionId;
    private Long userId;

    public UserSession(Address address, String name, String sessionId) {
        this.address = address;
        this.name = name;
        this.sessionId = sessionId;
    }

    public Address getAddress() {
        return address;
    }

    public String getName() {
        return name;
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
}
