package server.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {

    public static Connection getConnection() {
        try{
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());

            StringBuilder url = new StringBuilder();

            url.
                    append("jdbc:mysql://").		//db type
                    append("localhost:"). 			//host name
                    append("3306/").				//port
                    append("gameJavaDB?").			//db name
                    append("user=root&").			//login
                    append("password=root");        //password

            System.out.append("URL: " + url + "\n");

            Connection connection = DriverManager.getConnection(url.toString());
            return connection;
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static void connect(){
//        Connection connection = getConnection();
//        System.out.append("Connected!\n");
//        try {
//            System.out.println("Autocommit: " + connection.getAutoCommit());
//            System.out.println("DB name: " + connection.getMetaData().getDatabaseProductName());
//            System.out.println("DB version: " + connection.getMetaData().getDatabaseProductVersion());
//            System.out.println("Driver: " + connection.getMetaData().getDriverName());
//            connection.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }
}
