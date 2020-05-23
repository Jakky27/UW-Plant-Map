package edu.uw.cs403.plantmap.backend.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlantServerTest implements PlantServer{

    private HashMap<Integer, Plant> map = new HashMap<>();
    int index = 0;

    @Override
    public int registerPlant(String name, String description) throws Exception {

        Plant p = new Plant();
        p.setPlant_id(++index);
        p.setName(name);
        p.setDescription(description);
        map.put(index, p);
//        String output = String.format("register success!\n Plant name %s \n Description: %s\n", name, description);
//        System.out.println(output);
        return index;
    }

    @Override
    public Plant getPlantById(int plant_id) throws Exception {
        if (!map.containsKey(plant_id)) return null;
        return map.get(plant_id);
    }

    @Override
    public Plant getPlantByName(String name) throws Exception {
        return null;
    }

    @Override
    public int updatePlant(int id, String name, String description) throws Exception {
        if (!map.containsKey(id)) return 0;
        Plant p = map.get(id);
        p.setName(name);
        p.setDescription(description);
        return id;
    }

    @Override
    public int deletePlant(int plant_id) throws Exception {
        if (!map.containsKey(plant_id)) return 0;
        map.remove(plant_id);
        return plant_id;
    }

    @Override
    public List<Plant> getAllPlants() throws Exception {

        return new ArrayList<Plant>(map.values());
    }

}
