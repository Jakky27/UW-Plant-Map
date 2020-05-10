package edu.uw.cs403.plantmap.backend;

import static spark.Spark.*;

public class Application {
    public static void main(String[] args) {
        get("/", (req, res) -> "Welcome to the UW PlantMap API server.");
    }
}
