package server.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TExecutor {
    public <T> T execSelectQuery(Connection connection, String query, TResultHandler<T> handler) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(query);
        ResultSet result = stmt.getResultSet();
        T value = handler.handle(result);
        result.close();
        stmt.close();

        return value;
    }

    public void execUpdateQuery(Connection connection, String query) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(query);
        stmt.close();
    }

}
