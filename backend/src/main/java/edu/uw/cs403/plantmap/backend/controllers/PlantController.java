package edu.uw.cs403.plantmap.backend.controllers;

import edu.uw.cs403.plantmap.backend.models.Plant;
import edu.uw.cs403.plantmap.backend.models.PlantServer;
import spark.Request;
import spark.Response;
import org.json.*;

import java.sql.SQLException;
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
        if (!request.contentType().equals("application/json")){
            response.type("text/html");
            response.status(415);
            return 0;
        }

        try {
            JSONObject bodyJson = new JSONObject(request.body());
            int res = server.registerPlant(bodyJson.getString(attrName), bodyJson.getString(attrDescription));
            if (res == 0){
                response.status(404);
                return res;
            }
            response.type("text/plain");
            response.status(201);
            return res;
        } catch (Exception e){
            response.status(500);
            throw new Exception("Encountered an error when handler the request.", e);
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

        // If the id in url is not a valid int, it will catch the exception and return code 400
        try {
            int pid = Integer.parseInt(request.params(":id"));

            // This block catches SQL errors
            try {
                Plant p = server.getPlantById(pid);
                JSONObject res = new JSONObject(p);
                response.type("application/json");
                response.status(200);
                return res;
            }catch (SQLException se){
                response.status(500);
                throw new Exception("Encountered an error when handler the request.", se);
            }

        } catch (NumberFormatException ne) {
            response.status(400);
            throw new Exception("Encountered an error when handler the request.", ne);
        }

    }

    /**
     * Receives GET request to get all plants in database
     * @param request the http request
     * @param response the http response
     * @return a list of plant object in json array format
     * @throws Exception if there are any errors
     */
    public Object getAllPlant(Request request, Response response) throws Exception{

        // Try to catches any exceptions (basically SQL errors)
        try {
            List<Plant> list = server.getAllPlants();
            response.type("application/json");
            response.status(200);
            return new JSONArray(list);
        } catch (Exception e){
            response.status(500);
            throw new Exception("Encountered an error when handler the request.", e);
        }

    }

    /**
     * Receives PATCH request to update the information of a specific plant
     * @param request the http request
     * @param response the http response
     * @return
     * @throws Exception if there are any errors
     */
    public int updatePlant(Request request, Response response) throws Exception{
        // ensure the body type is JSON
        if (!request.contentType().equals("application/json")){
            response.type("text/html");
            response.status(415);
            return 0;
        }

        // This block catches parse errors
        try {
            int pid = Integer.parseInt(request.params(":id"));

            // This block catches SQL errors
            try {
                JSONObject bodyJson = new JSONObject(request.body());
                int res = server.updatePlant(pid, bodyJson.getString(attrName), bodyJson.getString(attrDescription));
                response.type("text/plain");
                response.status(200);
                return res;
            }catch (SQLException se){
                response.status(500);
                throw new Exception("Encountered an error when handler the request.", se);
            }

        } catch (NumberFormatException ne) {
            response.status(400);
            throw new Exception("Encountered an error when handler the request.", ne);
        }


    }

    /**
     * Receives DELETE request to delete a specific plant from database
     * @param request the http request
     * @param response the http response
     * @return
     * @throws Exception if there are any errors
     */
    public int deletePlant(Request request, Response response) throws Exception{

        // This block catches parse errors
        try {
            int pid = Integer.parseInt(request.params(":id"));

            // This block catches SQL errors
            try {
                int res = server.deletePlant(pid);
                response.type("text/plain");
                response.status(200);
                return res;
            }catch (SQLException se){
                response.status(500);
                throw new Exception("Encountered an error when handler the request.", se);
            }

        } catch (NumberFormatException ne) {
            response.status(400);
            throw new Exception("Encountered an error when handler the request.", ne);
        }


    }



}
