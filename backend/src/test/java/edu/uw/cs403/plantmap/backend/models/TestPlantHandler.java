package edu.uw.cs403.plantmap.backend.models;

import edu.uw.cs403.plantmap.backend.controllers.PlantController;
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

public class TestPlantHandler {

    private PlantServerTest server;
    private PlantController ctr;
    private Request mockRequest;
    private Response mockResponse;

    @SneakyThrows
    @Before
    public void initTest(){
        server = new PlantServerTest();
        ctr = new PlantController(server);
        mockRequest = Mockito.mock(Request.class);
        mockResponse = Mockito.mock(Response.class);
        server.registerPlant("test1", "desc1");
        server.registerPlant("test2", "desc2");
    }

    @Test
    public void testRegisterPlant(){

        String bodyContent = "{\n" +
                "  \"name\": \"test plant name\",\n" +
                "  \"description\": \"test description of plant\"\n" +
                "}";

        String badContent = "{\n" +
                "  \"filed1\": \"some\",\n" +
                "  \"filed2\": \"test\"\n" +
                "}";


        try {
        // success flow
        Mockito.when(mockRequest.requestMethod()).thenReturn("POST");
        Mockito.when(mockRequest.contentType()).thenReturn("application/json");
        Mockito.when(mockRequest.body()).thenReturn(bodyContent);
        int res = ctr.addPlant(mockRequest, mockResponse);
        assertEquals(3, res);

        // invalid content type
        Mockito.when(mockRequest.contentType()).thenReturn("text/html");
            res = ctr.addPlant(mockRequest, mockResponse);
        assertEquals(0, res);
        Mockito.verify(mockResponse).status(ArgumentMatchers.eq(415));

        // not valid json body
        Mockito.when(mockRequest.contentType()).thenReturn("application/json");
        Mockito.when(mockRequest.body()).thenReturn(badContent); // TODO: throw exception


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void testGetPlant() {
        try {
            Mockito.when(mockRequest.requestMethod()).thenReturn("GET");
            Mockito.when(mockRequest.params(":id")).thenReturn("1");
            Object p = ctr.getPlant(mockRequest, mockResponse);
            Object expect = new JSONObject(server.getPlantById(1));
            Mockito.verify(mockResponse).type("application/json");
//            assertEquals(expect, p);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testGetAllPlant() {

        try {
            Mockito.when(mockRequest.requestMethod()).thenReturn("GET");
            Object p = ctr.getAllPlant(mockRequest, mockResponse);
            Object expect = new JSONObject(server.getPlantById(1));
            Mockito.verify(mockResponse).type("application/json");
//            assertEquals(expect, p);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdatePlant() {
        String updateContent = "{\n" +
                "  \"name\": \"update plant name\",\n" +
                "  \"description\": \"update description of plant\"\n" +
                "}";

        try {
            Mockito.when(mockRequest.requestMethod()).thenReturn("POST");
            Mockito.when(mockRequest.body()).thenReturn(updateContent);
            Mockito.when(mockRequest.params(":id")).thenReturn("1");
            int res = ctr.updatePlant(mockRequest, mockResponse);
            assertEquals(1, res);
            assertEquals("update plant name",server.getPlantById(1).getName());
            assertEquals("update description of plant",server.getPlantById(1).getDescription());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testDeletePlant() {

        try {
            Mockito.when(mockRequest.requestMethod()).thenReturn("DELETE");
            Mockito.when(mockRequest.params(":id")).thenReturn("1");
            int res = ctr.deletePlant(mockRequest, mockResponse);
            assertEquals(1, res);
            assertNull(server.getPlantById(1));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
