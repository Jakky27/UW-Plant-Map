package edu.uw.cs403.plantmap.backend.controllers;

import edu.uw.cs403.plantmap.backend.models.Submission;
import edu.uw.cs403.plantmap.backend.models.SubmissionServer;
import org.json.JSONArray;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import java.sql.Date;
import java.util.List;

/**
 * Handles requests from client and calls related methods from the SubmissionServer to build up responses
 */

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

    /**
     * Receives POST request to create a new submission
     *
     * @param request the http request
     * @param response the http response
     * @return the id of the newly created post, or a failing message
     * @throws Exception if there are any errors
     */
    public Object createPost(Request request, Response response) throws Exception{
        response.type("text/html");
        if (request.contentType().equals("application/json")) {
            JSONObject bodyJson = new JSONObject(request.body());
            int res = server.createSubmission(bodyJson.getString(attrPostby), bodyJson.getLong(attrDate), bodyJson.getInt(attrPlantID), bodyJson.getFloat(attrLon), bodyJson.getFloat(attrLat));
            return res;
        } else {
            // response a http error
            response.status(415);
            return "Post failed";
        }
    }

    /**
     * Receives GET request to get the submission of a given id in the url
     *
     * @param request the http request
     * @param response the http response
     * @return the submission object in JSON format
     * @throws Exception if there are any errors
     */
    public Object getPost(Request request, Response response) throws Exception {
        int pid = Integer.parseInt(request.params(":id"));
        Submission s = server.getSubmission(pid);

        response.type("application/json");
        return new JSONObject(s);
    }

    /**
     * Receives GET request to get all the submissions in the database
     * @param request the http request
     * @param response the http response
     * @return the list of submissions object in JSON array format
     * @throws Exception if there are any errors
     */
    public Object getAllPost(Request request, Response response) throws Exception {
        String threshS = request.queryParams("reported");
        int thresh = 100;
        if (threshS != null && !threshS.isEmpty()) {
            thresh = Integer.parseInt(threshS);
        }
        List<Submission> list = server.getAllSubmission(thresh);

        response.type("application/json");
        return new JSONArray(list);
    }

    /**
     * Receives DELETE request to delete certain submission with the id in the url
     *
     * @param request the http request
     * @param response the http response
     * @return the submission id that has just been deleted
     * @throws Exception if there are any errors
     */
    public Object deletePost(Request request, Response response) throws Exception {
        int pid = Integer.parseInt(request.params(":id"));
        return server.deleteSubmission(pid);
    }

    /**
     *  Receives POST request for a submission report to increase the report number
     *
     * @param request
     * @param response
     * @return the submission id of the related report
     * @throws Exception if there are any errors
     */
    public Object reportPost(Request request, Response response) throws Exception {
        int pid = Integer.parseInt(request.params(":id"));
        return server.reportSubmission(pid);
    }


}
