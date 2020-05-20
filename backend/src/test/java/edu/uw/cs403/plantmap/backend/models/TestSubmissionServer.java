package edu.uw.cs403.plantmap.backend.models;

import edu.uw.cs403.plantmap.backend.Application;
import edu.uw.cs403.plantmap.backend.SQLConnectionPool;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class TestSubmissionServer {

    private SubmissionServerImp ssi;
    private PlantServerImp psi;

    @Before
    public void setUp() {
        SQLConnectionPool pool = Application.createConnectionPool();
        ssi = new SubmissionServerImp(pool);
        psi = new PlantServerImp(pool);

    }

    @Test
    public void testPostCycle(){
        try {
            // register the submission initially
            String name = "Rare Testing Plant";
            Date d = new Date();
            long input_date = d.getTime();
            int pid = psi.registerPlant("TestPlant", "TestPlant Description");

            int id = ssi.createSubmission("Test User",input_date, pid, (float)-122.335, (float)47.608);
            assertTrue(id > 0);

            Submission ids = ssi.getSubmission(id);
            assertNotNull(ids);

            int did = ssi.deleteSubmission(id);
            assertEquals(did, id);

        } catch(Exception E) {
            fail();
        }
    }

}
