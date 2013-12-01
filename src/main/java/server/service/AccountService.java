package server.service;

import server.Address;
import server.Subscriber;
import server.TimeHelper;
import server.message.MessageSystem;

import java.util.HashMap;
import java.util.Map;

public class AccountService implements Subscriber, Runnable {

    private class Account {
        private String name;
        private String pass;

        private Account(String name, String pass) {
            this.name = name;
            this.pass = pass;
        }

        private String getName() {
            return name;
        }

        private String getPass() {
            return pass;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Account account = (Account) o;

            if (!name.equals(account.name)) return false;
            if (!pass.equals(account.pass)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + pass.hashCode();
            return result;
        }
    }

    private Address address;
    private MessageSystem messageSystem;
    private Map<Account, Long> accountStorage = new HashMap<>(); // Account --> User ID

    public AccountService(MessageSystem messageSystem) {
        this.address = new Address();
        this.messageSystem = messageSystem;
        messageSystem.addService(this);
        messageSystem.getAddressService().setAddress(address);

        this.accountStorage.put(new Account("user0", "pass0"), 0L);
        this.accountStorage.put(new Account("user1", "pass1"), 1L);
        this.accountStorage.put(new Account("user2", "pass2"), 2L);

    }

    public void run() {
        while (true) {
            messageSystem.execForSubscriber(this);
            TimeHelper.sleep(10);
        }
    }

    public Long getUserId(String login, String password) {
        TimeHelper.sleep(5000);
        return accountStorage.get(new Account(login, password));
    }

    public Address getAddress() {
        return address;
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }
}
