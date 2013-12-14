package server.service;

import server.*;
import server.dao.ConnectDB;
import server.dao.UsersDAO;
import server.dao.UsersDataSet;
import server.message.MessageSystem;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.SQLException;

public class GameMechanics extends HttpServlet implements Subscriber, Runnable {

    private final Address address;
    private final MessageSystem messageSystem;

    public GameMechanics(MessageSystem messageSystem) {
        this.address = new Address();
        this.messageSystem = messageSystem;
        messageSystem.addService(this);
        messageSystem.getAddressService().setAddressGM(address);

    }
    public void printStuff(String stuff) {
        System.out.println("GM");
        System.out.println(stuff);
        System.out.println("GM");
    }

    public void run() {
        while (true) {
            messageSystem.execForSubscriber(this);
            TimeHelper.sleep(10);
        }
    }

    public Long getUserId(String login, String password) {
        TimeHelper.sleep(5000);
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
