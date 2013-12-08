package server.service;

import server.Address;
import server.Subscriber;
import server.TimeHelper;
import server.dao.ConnectDB;
import server.dao.UsersDAO;
import server.dao.UsersDataSet;
import server.message.MessageSystem;

import java.sql.Connection;
import java.sql.SQLException;


public class AccountService implements Subscriber, Runnable {

    // dead code?
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
    // end dead code TODO: test code

    private final Address address;
    private final MessageSystem messageSystem;

    public AccountService(MessageSystem messageSystem) {
        this.address = new Address();
        this.messageSystem = messageSystem;
        messageSystem.addService(this);
        messageSystem.getAddressService().setAddress(address);

    }

    public void run() {
        while (true) {
            messageSystem.execForSubscriber(this);
            TimeHelper.sleep(10);
        }
    }

    public Long getUserId(String login, String password) {
        //TimeHelper.sleep(5000);
        Connection connection = ConnectDB.getConnection();
        UsersDAO userDAO = new UsersDAO(connection);
        try
        {
            UsersDataSet result = userDAO.get(login, password);
            return result.getId();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public Address getAddress() {
        return address;
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }
}
