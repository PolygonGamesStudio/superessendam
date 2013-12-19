package server.dao;
//1. Тест конекта
//2. Тест наличия базы
//3. Тест записи
//4. Тест чтения
//5. удаление!!!

import org.junit.After;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

public class UsersDAOTest {

    @Before
    public void add_users(){
        Connection connection = ConnectDB.getConnection();
        UsersDAO userDAO = new UsersDAO(connection);
        try {
            userDAO.set("test_user1", "test_password1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSelect() {
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
