package edu.uw.cs403.plantmap.backend;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main {

    public static Connection startDbConnect() {
        // TODO: Add Azure DB information
        String hostName = "your_server.database.windows.net"; // update me
        String dbName = "your_database"; // update me
        String user = "your_username"; // update me
        String password = "your_password"; // update me
        String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;"
                + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url);
            String schema = connection.getSchema();
            System.out.println("Successful connection - Schema: " + schema);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }

    public static void main(String[] args){

        // start database connection
        Connection conn = startDbConnect();

        // TODO: add controllers

        // TODO: listen to the front end

    }
}
