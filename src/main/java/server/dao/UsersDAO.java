package server.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UsersDAO {

    private Connection con;

    public UsersDAO(Connection con){
        this.con = con;
    }
    //select * from users where name='dog' and password='cat';
    public UsersDataSet get(String user, String pass) throws SQLException{
        TExecutor exec = new TExecutor();
        return exec.execSelectQuery(con, "select * from users where name='" + user + "' and password='" + pass + "';", new TResultHandler<UsersDataSet>() {
            public UsersDataSet handle(ResultSet result) throws SQLException {
                result.next(); // ???
                return new UsersDataSet(result.getLong(1), result.getString(2), result.getString(3));
            }

        });
    }

    //INSERT INTO users (name, password) VALUES ('foo', 'bar');
    public void set(String user, String pass) throws SQLException{
        TExecutor exec = new TExecutor();
        exec.execUpdateQuery(con, "INSERT INTO users (name, password) VALUES ('" + user +"','"+ pass+"');");
    }

    //DELETE FROM <Имя Таблицы> WHERE <Условие отбора записей>
    public void delete(String user) throws SQLException{
        TExecutor exec = new TExecutor();
        exec.execUpdateQuery(con, "DELETE FROM users where name = '" + user +"';");
    }
}
