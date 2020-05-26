package edu.uw.cs403.plantmap.backend.models;

import edu.uw.cs403.plantmap.backend.controllers.PlantController;
import edu.uw.cs403.plantmap.backend.controllers.SubmissionController;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import spark.Request;
import spark.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestSubmissionHandler {

    private SubmissionServerTest server;
    private SubmissionController ctr;
    private Request mockRequest;
    private Response mockResponse;

    @SneakyThrows
    @Before
    public void initTest(){
        server = new SubmissionServerTest();
        ctr = new SubmissionController(server);
        mockRequest = Mockito.mock(Request.class);
        mockResponse = Mockito.mock(Response.class);
        server.createSubmission("user1",1000002000, 1, 11.3225f, 45.221f);
        server.createSubmission("user2",1345002000, 2, 99.3225f, -34.774f);
    }

    @Test
    public void testCreatePost(){
        String content = "{\n" +
                "  \"posted_by\": \"user3\",\n" +
                "  \"posted_on\": 899029482,\n" +
                "  \"plant_id\": 2,\n" +
                "  \"longitude\": 18.990f,\n" +
                "  \"latitude\": 23.988f\n" +
                "}";

        try {
            // success flow
            Mockito.when(mockRequest.requestMethod()).thenReturn("POST");
            Mockito.when(mockRequest.contentType()).thenReturn("application/json");
            Mockito.when(mockRequest.body()).thenReturn(content);
            Object res = ctr.createPost(mockRequest, mockResponse);
            assertEquals(3, res);

            // invalid content type
            Mockito.when(mockRequest.contentType()).thenReturn("text/html");
            res = ctr.createPost(mockRequest, mockResponse);
            assertEquals("Post failed", res);
            Mockito.verify(mockResponse).status(ArgumentMatchers.eq(415));


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void testGetPost(){
        try {
            Mockito.when(mockRequest.requestMethod()).thenReturn("GET");
            Mockito.when(mockRequest.params(":id")).thenReturn("1");
            Object sub = ctr.getPost(mockRequest, mockResponse);
            JSONObject expected = new JSONObject(server.getSubmission(1));
            Mockito.verify(mockResponse).type("application/json");
            assertEquals(sub.toString(), expected.toString());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testGetAllPost() {

        try {
            Mockito.when(mockRequest.requestMethod()).thenReturn("GET");
            Object p = ctr.getAllPost(mockRequest, mockResponse);
            Mockito.verify(mockResponse).type("application/json");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testDeletePost() {

        try {
            Mockito.when(mockRequest.requestMethod()).thenReturn("DELETE");
            Mockito.when(mockRequest.params(":id")).thenReturn("1");
            Object res = ctr.deletePost(mockRequest, mockResponse);
            assertEquals(1, res);
            assertNull(server.getSubmission(1));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
