package server;

import java.util.HashMap;
import java.util.Map;

public class AccountService implements Subscriber, Runnable {
    private Address address;
    private MessageSystem messageSystem;
    private Map<String, Long> accountStorage = new HashMap<>(); // Account --> User ID
    private Map<String, String> loginToPassword = new HashMap<>(); // Login --> Password


    public AccountService(MessageSystem messageSystem) {
        this.address = new Address();
        this.messageSystem = messageSystem;
        // TODO: add something about message system
        String[] users = {"user0", "user1", "user2"};

        this.accountStorage.put(users[0], 0L);
        this.accountStorage.put(users[1], 1L);
        this.accountStorage.put(users[2], 2L);

        this.loginToPassword.put(users[0], "user0pwd");
        this.loginToPassword.put(users[1], "user1pwd");
        this.loginToPassword.put(users[2], "user2pwd");
    }

    public void run() {
        // TODO: code run method
    }

    public Long getUserId(String account) {
        // TODO: add time helper to wait for 5 seconds
        return accountStorage.get(account);
    }

    public boolean correctPassword(String login, String password) {
        return loginToPassword.get(login).equals(password);
    }

    public Address getAddress() {
        return address;
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }
}
