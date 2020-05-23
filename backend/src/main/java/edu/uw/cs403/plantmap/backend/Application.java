package edu.uw.cs403.plantmap.backend;

import edu.uw.cs403.plantmap.backend.controllers.ImageController;
import edu.uw.cs403.plantmap.backend.controllers.PlantController;
import edu.uw.cs403.plantmap.backend.controllers.SubmissionController;
import edu.uw.cs403.plantmap.backend.models.PlantServerImp;
import edu.uw.cs403.plantmap.backend.models.SubmissionServerImp;

import java.io.PrintWriter;
import java.io.StringWriter;

import static spark.Spark.*;

public class Application {
    public static void main(String[] args) {
        SQLConnectionPool pool = createConnectionPool();
        SQLBootstrapper bootstrap = new SQLBootstrapper(pool);

        bootstrap.createTablesOnFirstRun();

        port(getHerokuAssignedPort());

        get("/", (req, res) -> "UW PlantMap API server");

        // Plant controller
        PlantController plantCtr = new PlantController(new PlantServerImp(pool));

        post("/v1/plant", plantCtr::addPlant);
        get("/v1/plant/:id", plantCtr::getPlant);
        delete("/v1/plant/:id", plantCtr::deletePlant);
        put("/v1/plant/:id", plantCtr::updatePlant);
        get("/v1/plant", plantCtr::getAllPlant);

        // Submission controller
        SubmissionController subCtr = new SubmissionController(new SubmissionServerImp(pool));

        post("/v1/submission", subCtr::createPost);
        get("/v1/submission/:id",subCtr::getPost);
        delete("/v1/submission/:id", subCtr::deletePost);
        get("/v1/submission", subCtr::getAllPost);

        // Image controller
        ImageController imgCtr = new ImageController(new SubmissionServerImp(pool));

        get("/v1/image/:id", imgCtr::getImg);
        post("/v1/image/:id", imgCtr::uploadImg);

        exception(Exception.class, (exception, request, response) -> {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            pw.println("500 - Internal Error");
            pw.println();
            exception.printStackTrace(pw);
            exception.printStackTrace();

            response.status(500);
            response.type("text/plain");
            response.body(sw.toString());
        });
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }

        return 4567; // Return default port if heroku-port isn't set (i.e. on localhost)
    }

    public static SQLConnectionPool createConnectionPool() {
        String hostName = System.getenv("DB_HOST");
        String dbName = System.getenv("DB_NAME");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        if (hostName == null || dbName == null || user == null || password == null) {
            System.out.println("Failed to load database parameters for connection!\nMake sure the DB_HOST, DB_NAME, " +
                    "DB_USER, DB_PASSWORD environment variables are set.");
            System.exit(1);
        }

        return new SQLConnectionPool(hostName, dbName, user, password);
    }
}
