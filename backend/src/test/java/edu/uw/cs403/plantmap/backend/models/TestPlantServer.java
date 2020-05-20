package edu.uw.cs403.plantmap.backend.models;

import edu.uw.cs403.plantmap.backend.Application;
import edu.uw.cs403.plantmap.backend.SQLConnectionPool;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestPlantServer {

    private PlantServerImp psi;

    @Before
    public void setUp() {
        SQLConnectionPool pool = Application.createConnectionPool();
        psi = new PlantServerImp(pool);
    }

    @Test
    public void testRegisterPlant(){
        try {
            // register the plant initially
            String name = "Rare Testing Plant";
            int id = psi.registerPlant(name, "This is a testing plant description");
            assertTrue(id > 0);

            Plant idp = psi.getPlantById(id);
            assertNotNull(idp);

            Plant np = psi.getPlantByName(name);
            assertNotNull(np);
            assertEquals(idp.hashCode(), np.hashCode());

            int did = psi.deletePlant(id);
            assertEquals(did, id);

        } catch(Exception E) {
            fail();
        }
    }

}
