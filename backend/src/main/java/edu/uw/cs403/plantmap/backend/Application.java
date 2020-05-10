package edu.uw.cs403.plantmap.backend;

import static spark.Spark.*;

public class Application {
    public static void main(String[] args) {
        port(getHerokuAssignedPort());

        get("/", (req, res) -> "Welcome to the UW PlantMap API server.");
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }

        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}
