package edu.uw.cs403.plantmap.backend.controllers;

import edu.uw.cs403.plantmap.backend.models.Plant;
import edu.uw.cs403.plantmap.backend.models.PlantServer;
import spark.Request;
import spark.Response;
import org.json.*;

import java.text.Format;
import java.util.List;

public class PlantController {

    private PlantServer server;
    private static String attrID = "plant_id";
    private static String attrName = "name";
    private static String attrDescription = "description";

    public PlantController(PlantServer server) {
        this.server = server;
    }


    public int addPlant(Request request, Response response) throws Exception {
        // ensure the body type is JSON
        if (request.contentType().equals("application/json")) {
            JSONObject bodyJson = new JSONObject(request.body());
            server.registerPlant(bodyJson.getString(attrName), bodyJson.getString(attrDescription));
            Plant newPlant = server.getPlantByName(bodyJson.getString(attrName));
            return newPlant.getPlant_id();
        } else {
            // response a http error
            response.type("text/html");
            response.status(415);
            return 0;
        }
    }

    public Object getPlant(Request request, Response response) throws Exception {
        int pid = Integer.parseInt(request.params(":id"));
        Plant p = server.getPlantById(pid);
        JSONObject jo = new JSONObject(p);
        return jo;
    }

    //TODO: get all plants
    public List<JSONObject> getAllPlant(Request request, Response response) throws Exception{
        List<Plant> list = server.getAllPlants();
        return null;
    }

    public int updatePlant(Request request, Response response) throws Exception{
        int pid = Integer.parseInt(request.params(":id"));
        JSONObject bodyJson = new JSONObject(request.body());
        return server.updatePlant(pid, bodyJson.getString(attrName), bodyJson.getString(attrDescription));
    }

    public int deletePlant(Request request, Response response) throws Exception{
        int pid = Integer.parseInt(request.params(":id"));
        return server.deletePlant(pid);
    }



}
