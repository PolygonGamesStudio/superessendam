package server.service;

import server.Address;
import server.Subscriber;
import server.TimeHelper;
import server.base.AccountService;
import server.dao.ConnectDB;
import server.dao.UsersDAO;
import server.dao.UsersDataSet;
import server.message.MessageSystem;

import java.sql.Connection;
import java.sql.SQLException;


public class AccountServiceImpl implements Subscriber, Runnable, AccountService {

    private final Address address;
    //    private Address address;
    private final MessageSystem messageSystem;
//    private MessageSystem messageSystem;

    public AccountServiceImpl(MessageSystem messageSystem) {
        this.address = new Address();
        this.messageSystem = messageSystem;
        messageSystem.addService(this);
        messageSystem.getAddressService().setAddressAS(address);

    }

    public void run() {
        while (true) {
            messageSystem.execForSubscriber(this);
            TimeHelper.sleep(10);
        }
    }

    public Long getUserId(String login, String password) {
        TimeHelper.sleep(500);
        Connection connection = ConnectDB.getConnection();
        UsersDAO userDAO = new UsersDAO(connection);
        try {
            UsersDataSet result = userDAO.get(login, password);
            return result.getId();
        } catch (SQLException e) {
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
