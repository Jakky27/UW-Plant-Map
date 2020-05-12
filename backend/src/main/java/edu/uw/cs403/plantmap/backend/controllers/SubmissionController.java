package edu.uw.cs403.plantmap.backend.controllers;

import edu.uw.cs403.plantmap.backend.models.Plant;
import edu.uw.cs403.plantmap.backend.models.SubmissionServer;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import java.sql.Date;

public class SubmissionController {

    private SubmissionServer server;

    // filed name for Submission class
    private static String attrID = "post_id";
    private static String attrPostby = "posted_by";
    private static String attrDate = "post_date";
    private static String attrImg = "img";
    private static String attLon = "longitude";
    private static String attrLat = "latitude";

    public int createPost(Request request, Response response) {
        if (request.contentType().equals("application/json")) {
            JSONObject bodyJson = new JSONObject(request.body());
            Date date = new Date(bodyJson.getLong(attrDate));
            server.createSubmission(bodyJson.getString(attrPostby), date);
            Plant newPlant = server.getPlantByName(bodyJson.getString(attrName));
            return newPlant.getPlant_id();
        } else {
            // response a http error
            response.type("text/html");
            response.status(415);
            return 0;
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
