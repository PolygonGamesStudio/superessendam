package server.dao;

import resource.DbInfo;
import server.base.Resource;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {

    public static Connection getConnection(Resource connectionInfo) {
        try {
            DbInfo connInfo = null;
            if(connectionInfo instanceof DbInfo)
            {
                connInfo = (DbInfo)connectionInfo;
            }
            DriverManager.registerDriver((Driver) Class.forName("com." +
                                                                connInfo.type +
                                                                ".jdbc.Driver").newInstance());

            StringBuilder url = new StringBuilder();

            url.
                    append("jdbc:"+ connInfo.type + "://").
                    append(connInfo.address + ":").
                    append(connInfo.port +"/").
                    append(connInfo.dataBaseName +"?").
                    append("user=" + connInfo.user + "&").
                    append("password=" + connInfo.password);   // FIXME: need system resources

            System.out.println(url.toString());
            Connection connection = DriverManager.getConnection(url.toString());
            return connection;
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
