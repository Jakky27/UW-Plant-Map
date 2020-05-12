package edu.uw.cs403.plantmap.backend.models;

import java.util.ArrayList;
import java.util.List;

public class PlantServerTest implements PlantServer{

    private List<Plant> store = new ArrayList<>();

    @Override
    public void registerPlant(String name, String description) throws Exception {
        int id = store.size()+1;
        Plant p = new Plant();
        p.setPlant_id(id);
        p.setName(name);
        p.setDescription(description);
        store.add(p);
        String output = String.format("register success!\n Plant name %s \n Description: %s\n", name, description);
        System.out.println(output);
    }

    @Override
    public Plant getPlantById(int plant_id) throws Exception {
        if (store.size() <= plant_id - 1) return null;
        return store.get(plant_id-1);
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
