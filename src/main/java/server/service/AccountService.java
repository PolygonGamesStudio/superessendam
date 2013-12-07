package server.service;

import server.Address;
import server.Subscriber;
import server.TimeHelper;
import server.message.MessageSystem;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AccountService implements Subscriber, Runnable {

    private class Account {
        private final String name;
        private final String pass;

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

    private final Address address;
    private final MessageSystem messageSystem;
    private Map<Account, Long> accountStorage = new HashMap<>(); // Account --> User ID

    public AccountService(MessageSystem messageSystem) {
        this.address = new Address();
        this.messageSystem = messageSystem;
        messageSystem.addService(this);
        messageSystem.getAddressService().setAddress(address);

        this.accountStorage.put(new Account("user0", "pass0"), 0L);
        this.accountStorage.put(new Account("user1", "pass1"), 1L);
        this.accountStorage.put(new Account("user2", "pass2"), 2L);

//        //TODO: from here
//        Driver driver = null;
//        try {
//            driver = (Driver) Class.forName("com.mysql.jdbc.Driver").newInstance();
//        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
//            e.printStackTrace();
//            // TODO: handle exception here
//        }
//        try {
//            DriverManager.registerDriver(driver);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            // TODO: handle exception here
//        }
//        StringBuilder url = new StringBuilder();
//        url.append("localhost");
//
//        Connection connection = null;
//        try {
//            connection = DriverManager.getConnection(url.toString());
//        } catch (SQLException e) {
//            e.printStackTrace();
//            // TODO: handle exception here
//        }

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
