package server.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import resource.ResourceSystemImpl;
import server.base.ResourceSystem;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class UsersDAOTest {

    private Connection connection;


    @Before
    public void add_user() throws SQLException {
        ResourceSystem resourceSystem = new ResourceSystemImpl();
        connection = ConnectDB.getConnection(resourceSystem.getResource("testDB.xml"));
        UsersDAO userDAO = new UsersDAO(connection);
        userDAO.createTable("users");
        userDAO.set("test_user1", "test_password1");

    }

    @Test
    public void select_user() throws SQLException {
        UsersDAO userDAO = new UsersDAO(connection);
        UsersDataSet result = userDAO.get("test_user1", "test_password1");
        assertEquals(result.getName(), "test_user1");
        assertEquals(result.getPassword(), "test_password1");
    }

    @After
    public void del_user() throws SQLException {
        UsersDAO userDAO = new UsersDAO(connection);
        userDAO.delete("test_user1");
        userDAO.dropTable("users");
    }
}
