package edu.uw.cs403.plantmap.backend.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlantServerTest implements PlantServer{

    private List<Plant> store = new ArrayList<>();
    private HashMap<String, Plant> map = new HashMap<>();

    @Override
    public int registerPlant(String name, String description) throws Exception {
        int id = store.size()+1;
        Plant p = new Plant();
        p.setPlant_id(id);
        p.setName(name);
        p.setDescription(description);
        store.add(p);
        map.put(name, p);
//        String output = String.format("register success!\n Plant name %s \n Description: %s\n", name, description);
//        System.out.println(output);
        return id;
    }

    @Override
    public Plant getPlantById(int plant_id) throws Exception {
        if (store.size() <= plant_id - 1) return null;
        return store.get(plant_id-1);
    }

    @Override
    public Plant getPlantByName(String name) throws Exception {
        if (!map.containsKey(name)){
            return null;
        } else{
            return map.get(name);
        }
    }

    @Override
    public int updatePlant(int id, String name, String description) throws Exception {
        if (id-1 >= store.size()) {
            return 0;
        }
        map.remove(name);
        Plant p = store.get(id - 1);
        p.setName(name);
        p.setDescription(description);
        map.put(name, p);
        return id;
    }

    @Override
    public int deletePlant(int plant_id) throws Exception {
        if (plant_id-1 >= store.size()) {
            return 0;
        }else{
            store.remove(plant_id -1 );
            return plant_id;
        }
    }

    @Override
    public List<Plant> getAllPlants() throws Exception {

        return store;
    }
}
