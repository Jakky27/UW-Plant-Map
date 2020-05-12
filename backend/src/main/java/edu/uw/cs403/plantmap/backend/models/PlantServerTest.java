package edu.uw.cs403.plantmap.backend.models;

import java.util.List;

public class PlantServerTest implements PlantServer{
    @Override
    public void registerPlant(String name, String description) throws Exception {
        String output = String.format("register success!\n Plant name %s \n Description: %s\n", name, description);
        System.out.println(output);
    }

    @Override
    public Plant getPlantById(int plant_id) throws Exception {
        Plant plant = new Plant();
        plant.setName("test");
        plant.setName("test description");
        plant.setPlant_id(9999);
        return plant;
    }

    @Override
    public Plant getPlantByName(String name) throws Exception {
        return null;
    }

    @Override
    public int updatePlant(int id, String name, String description) throws Exception {
        return 0;
    }

    @Override
    public int deletePlant(int plant_id) throws Exception {
        return 0;
    }

    @Override
    public List<Plant> getAllPlants() throws Exception {
        return null;
    }
}
