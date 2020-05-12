package edu.uw.cs403.plantmap.backend;

import edu.uw.cs403.plantmap.backend.controllers.PlantController;
import edu.uw.cs403.plantmap.backend.models.PlantServerImp;
import edu.uw.cs403.plantmap.backend.models.PlantServerTest;

import java.sql.Connection;
import java.sql.DriverManager;

import static spark.Spark.*;

public class Application {
    public static void main(String[] args) {
        port(getHerokuAssignedPort());

        get("/", (req, res) -> "Welcome to the UW PlantMap API server.");


        try {
            // start database connection, comment
            Connection conn = startDbConnect(); // comment me for testing

            // TODO: add controllers
            PlantController plantCtr = new PlantController(new PlantServerImp(conn));

            post("/v1/plant", (req, res) -> plantCtr.addPlant(req, res));
            get("/v1/plant/:id", (req, res) -> plantCtr.getPlant(req, res));
            delete("/v1/plant/:id", (req, res) -> plantCtr.deletePlant(req, res));
            put("/v1/plant/:id", (req, res) -> plantCtr.updatePlant(req, res));

            // Test without DB
//            PlantController plantCtrTest = new PlantController(new PlantServerTest());
//            post("/v1/plant", plantCtrTest::addPlant);
//            get("/v1/plant/:id", plantCtrTest::getPlant);
//            get("/v1/plant", plantCtrTest::getAllPlant);
//            delete("/v1/plant/:id", plantCtrTest::deletePlant);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }

        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }

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
}
