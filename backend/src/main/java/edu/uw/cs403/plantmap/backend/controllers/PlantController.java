package edu.uw.cs403.plantmap.backend.controllers;

import edu.uw.cs403.plantmap.backend.models.Plant;
import edu.uw.cs403.plantmap.backend.models.PlantServer;
import spark.Request;
import spark.Response;
import org.json.*;

import java.util.List;

/**
 * Handles requests from client and calls related methods from the PlantServer to build up responses
 */

public class PlantController {

    private PlantServer server;
    private static String attrName = "name";
    private static String attrDescription = "description";

    public PlantController(PlantServer server) {
        this.server = server;
    }


    /**
     * Receive POST request to add a new plant in the database
     * @param request the http request
     * @param response the http response
     * @return the plant_id just added, or 0 if the request body is not valid json format
     * @throws Exception if there are any errors
     */
    public int addPlant(Request request, Response response) throws Exception {
        // ensure the body type is JSON
        if (request.contentType().equals("application/json")) {
            JSONObject bodyJson = new JSONObject(request.body());
            int res = server.registerPlant(bodyJson.getString(attrName), bodyJson.getString(attrDescription));
            return res;
        } else {
            // response a http error
            response.type("text/html");
            response.status(415);
            return 0;
        }
    }

    /**
     * Receives GET request to get a specific plant with the given id provided in the url
     * @param request the http request
     * @param response the http response
     * @return a plant object in json format
     * @throws Exception if there are any errors
     */
    public Object getPlant(Request request, Response response) throws Exception {
        int pid = Integer.parseInt(request.params(":id"));
        Plant p = server.getPlantById(pid);

        response.type("application/json");
        return new JSONObject(p);
    }

    /**
     * Receives GET request to get all plants in database
     * @param request the http request
     * @param response the http response
     * @return a list of plant object in json array format
     * @throws Exception if there are any errors
     */
    public Object getAllPlant(Request request, Response response) throws Exception{
        List<Plant> list = server.getAllPlants();
        response.type("application/json");

        return new JSONArray(list);
    }

    /**
     * Receives PATCH request to update the information of a specific plant
     * @param request the http request
     * @param response the http response
     * @return
     * @throws Exception if there are any errors
     */
    public int updatePlant(Request request, Response response) throws Exception{
        int pid = Integer.parseInt(request.params(":id"));
        JSONObject bodyJson = new JSONObject(request.body());
        return server.updatePlant(pid, bodyJson.getString(attrName), bodyJson.getString(attrDescription));
    }

    /**
     * Receives DELETE request to delete a specific plant from database
     * @param request the http request
     * @param response the http response
     * @return
     * @throws Exception if there are any errors
     */
    public int deletePlant(Request request, Response response) throws Exception{
        int pid = Integer.parseInt(request.params(":id"));
        return server.deletePlant(pid);
    }



}
