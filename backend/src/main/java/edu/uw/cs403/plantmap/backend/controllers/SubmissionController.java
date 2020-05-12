package edu.uw.cs403.plantmap.backend.controllers;

import edu.uw.cs403.plantmap.backend.models.Plant;
import edu.uw.cs403.plantmap.backend.models.SubmissionServer;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import java.sql.Date;

public class SubmissionController {

    private SubmissionServer server;

    public SubmissionController(SubmissionServer server) {
        this.server = server;
    }

    // JSON keys for Submission class
    private static String attrID = "sub_id";
    private static String attrPostby = "posted_by";
    private static String attrDate = "posted_on";
    private static String attrPlantID = "plant_id";
    private static String attrImg = "img";
    private static String attrLon = "longitude";
    private static String attrLat = "latitude";

    public Object createPost(Request request, Response response) throws Exception{
        response.type("text/html");
        if (request.contentType().equals("application/json")) {
            JSONObject bodyJson = new JSONObject(request.body());
            Date date = new Date(bodyJson.getLong(attrDate));
            server.createSubmission(bodyJson.getString(attrPostby), date, bodyJson.getInt(attrPlantID), bodyJson.getFloat(attrLon), bodyJson.getFloat(attrLat));
            return "Successfully posted!";
        } else {
            // response a http error
            response.status(415);
            return "Post failed";
        }
    }

    public Object getPost(Request request, Response response) {
        return 0;
    }

    public Object getAllPost(Request request, Response response) {
        return 0;
    }

    public Object deletePost(Request request, Response response) {
        return 0;
    }

}
