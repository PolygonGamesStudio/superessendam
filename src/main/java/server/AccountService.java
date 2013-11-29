package server;

import java.util.HashMap;
import java.util.Map;

public class AccountService implements Subscriber, Runnable {
    private Address address;
    private MessageSystem messageSystem;
    private Map<String, Long> accountStorage = new HashMap<>(); // Account --> User ID


    public AccountService(MessageSystem messageSystem) {
        this.address = new Address();
        this.messageSystem = messageSystem;
        // TODO: add something about message system
        this.accountStorage.put("user0", 0L);
        this.accountStorage.put("user2", 1L);
        this.accountStorage.put("user2", 2L);
    }

    public void run() {
        // TODO: code run method
    }

    public Long getUserId(String account) {
        // TODO: add time helper to wait for 5 seconds
        return accountStorage.get(account);
    }

    public Address getAddress() {
        return address;
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }
}
