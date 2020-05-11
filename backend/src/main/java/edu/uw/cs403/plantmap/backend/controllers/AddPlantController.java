package edu.uw.cs403.plantmap.backend.controllers;

import edu.uw.cs403.plantmap.backend.models.Plant;
import edu.uw.cs403.plantmap.backend.models.PlantServer;
import spark.Request;
import spark.Response;
import spark.Route;
import org.json.*;

import java.text.Format;

public class AddPlantController implements Route {

    private PlantServer server;
    private static String attrID = "plant_id";
    private static String attrName = "name";
    private static String attrDescription = "description";

    public AddPlantController(PlantServer server) {
        this.server = server;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        // handle different methods
        switch (request.requestMethod()) {
            // Use post method to add a new plant into database
            case "POST":
                // ensure the body type is JSON
                if (request.contentType().equals("application/json")){
                    JSONObject bodyJson = new JSONObject(request.body());
                    server.registerPlant(bodyJson.getString(attrName), bodyJson.getString(attrDescription));
                    Plant newPlant = server.getPlantByName(bodyJson.getString(attrName));
                    return newPlant.getPlant_id();
                }else{
                    // response a http error
                    response.type("text/html");
                    response.status(415);
                    return 0;
                }

            //TODO: implement GET method, get all plants
            case "GET":
                break;

            default:
                response.status(405);
                return 0;


        }

        return 0;
    }
}
