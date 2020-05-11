package edu.uw.cs403.plantmap.backend;

import edu.uw.cs403.plantmap.backend.controllers.AddPlantController;
import edu.uw.cs403.plantmap.backend.models.PlantServerImp;
import edu.uw.cs403.plantmap.backend.models.PlantServerTest;

import java.sql.Connection;
import java.sql.DriverManager;
import static spark.Spark.*;

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

        // start database connection, comment
        Connection conn = startDbConnect(); // comment me for testing

        // TODO: add controllers
        post("/v1/plant", new AddPlantController(new PlantServerImp(conn))); // comment me for testing
//        post("/v1/plant", new AddPlantController(new PlantServerTest())); // for testing without DB


    }
}
