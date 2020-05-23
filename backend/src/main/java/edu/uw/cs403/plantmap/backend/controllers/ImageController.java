package edu.uw.cs403.plantmap.backend.controllers;

import edu.uw.cs403.plantmap.backend.models.Submission;
import edu.uw.cs403.plantmap.backend.models.SubmissionServer;
import spark.Request;
import spark.Response;

public class ImageController {

    private SubmissionServer server;

    public ImageController(SubmissionServer server) {
        this.server = server;
    }

    public Object uploadImg(Request request, Response response) throws Exception {
        // parse submission id from URL
        int sub_id = Integer.parseInt(request.params(":id"));
        server.updateSubmission(sub_id, request.bodyAsBytes());
        response.type("text/plain");
        return sub_id;
    }

    public Object getImg(Request request, Response response) throws Exception {
        int sub_id = Integer.parseInt(request.params(":id"));
        response.type("application/octet-stream");
        return server.getSubmissionImage(sub_id);
    }

}
