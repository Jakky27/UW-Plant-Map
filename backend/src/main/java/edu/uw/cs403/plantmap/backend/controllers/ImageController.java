package edu.uw.cs403.plantmap.backend.controllers;

import edu.uw.cs403.plantmap.backend.models.Submission;
import edu.uw.cs403.plantmap.backend.models.SubmissionServer;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

public class ImageController {

    private SubmissionServer server;

    public ImageController(SubmissionServer server) {
        this.server = server;
    }

    /**
     * Receives POST request to add an image into a submission with the id in the url
     * @param request the http request
     * @param response the http response
     * @return the submissionID of the related submission for this image
     * @throws Exception if there are any errors
     */
    public Object uploadImg(Request request, Response response) throws Exception {

        // Try to catch parse errors
        try {
            int sub_id = Integer.parseInt(request.params(":id"));

            // This block catches SQL errors
            try {
                int res = server.updateSubmission(sub_id, request.bodyAsBytes());
                response.type("text/plain");
                response.status(201);
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
     * Receives GET request to get a image of the given id in the url
     * @param request the http request
     * @param response the http response
     * @return the bytes of the images, or empty bytes if there isn't any images in the database
     * @throws Exception if there are any errors
     */
    public Object getImg(Request request, Response response) throws Exception {

        // Try to catch parse errors
        try {
            int sub_id = Integer.parseInt(request.params(":id"));

            // This block catches SQL errors
            try {
                byte[] res = server.getSubmissionImage(sub_id);
                response.type("image/jpeg");
                response.status(201);
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
