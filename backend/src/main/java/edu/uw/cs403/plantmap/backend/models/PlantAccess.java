package edu.uw.cs403.plantmap.backend.models;

import java.util.List;
import java.util.UUID;

/*
* PlantAccess access the plant data in database
* */
public interface PlantAccess {

    /*
    * registerPlant registers a new plant into the database, and return the plant_id if success
    */
    public UUID registerPlant(String name, String description) throws Exception;

    /*
    * GetPlant searches for a plant
    */
    public Plant getPlantById(UUID plant_id) throws Exception;

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
    public UUID updatePlant(String name, String description) throws Exception;

    /*
    * deletePlant delete the record of given plant id
     */
    public UUID deletePlant(UUID plant_id) throws Exception;

    /*
    * GetAllPlants gets all plant from database
    */
    public List<Plant> getAllPlants() throws Exception;

}
