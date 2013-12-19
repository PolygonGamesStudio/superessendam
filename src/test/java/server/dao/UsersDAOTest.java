package server.dao;

import org.junit.After;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

public class UsersDAOTest {

    @Before
    public void add_user(){
        Connection connection = ConnectDB.getConnection();
        UsersDAO userDAO = new UsersDAO(connection);
        try {
            userDAO.set("test_user1", "test_password1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void select_user() {
        Connection connection = ConnectDB.getConnection();
        UsersDAO userDAO = new UsersDAO(connection);
        try {
            UsersDataSet result = userDAO.get("test_user1", "test_password1");
            assertEquals(result.getName(), "test_user1");
            assertEquals(result.getPassword(), "test_password1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @After
    public void del_user() {
        Connection connection = ConnectDB.getConnection();
        UsersDAO userDAO = new UsersDAO(connection);
        try {
            userDAO.delete("test_user1");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
