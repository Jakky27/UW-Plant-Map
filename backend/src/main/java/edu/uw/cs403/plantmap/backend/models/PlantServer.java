package edu.uw.cs403.plantmap.backend.models;

import java.util.List;
import java.util.UUID;

/*
* PlantAccess access the plant data in database
* */
public interface PlantServer {

    /*
    * registerPlant registers a new plant into the database
    */
    public void registerPlant(String name, String description) throws Exception;

    /*
    * GetPlant searches for a plant
    */
    public Plant getPlantById(int plant_id) throws Exception;

    /*
     * GetPlant searches for a plant
     */
    public Plant getPlantByName(String name) throws Exception;

    /*
    * UpdatePlant update the name or/and description of a registered plant
    * @param name name of the plant
    * @param description more detailed information of the plant
    * @return the id of updated plant
    */
    public int updatePlant(int id, String name, String description) throws Exception;

    /*
    * deletePlant delete the record of given plant id
     */
    public int deletePlant(int plant_id) throws Exception;

    /*
    * GetAllPlants gets all plant from database
    */
    public List<Plant> getAllPlants() throws Exception;

}
