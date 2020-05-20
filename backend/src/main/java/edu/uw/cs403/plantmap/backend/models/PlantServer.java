package edu.uw.cs403.plantmap.backend.models;

import java.util.List;

/*
* PlantAccess access the plant data in database
* */
public interface PlantServer {

    /**
     * registers a new plant to the database
     *
     * @param name          name of the plant
     * @param description   description of the plant
     * @return              returns the plant_id of the added plant, else returns 0
     * @throws SQLException if there is an error when executing SQL statements
     */
    public int registerPlant(String name, String description) throws Exception;

    /**
     * retrieves a new plant based on the given id
     *
     * @param plant_id      primary key of the plant to be returned
     * @return              on success, returns the plant, else throws exception
     * @throws SQLException if there is an error when executing SQL statements
     */
    public Plant getPlantById(int plant_id) throws Exception;

    /**
     * returns the plant based on name
     *
     * @param name          name of the plant
     * @return              on success, returns plant, else throws exception
     * @throws SQLException if there is an error when executing SQL statements
     */
    public Plant getPlantByName(String name) throws Exception;

    /**
     * UpdatePlant update the name or/and description of a registered plant
     *
     * @param id            primary key of the plant in the database
     * @param name          name of the plant
     * @param description   more detailed information of the plant
     * @return              the id of updated plant
     * @throws SQLException if there is an error when executing SQL statements
     */
    public int updatePlant(int id, String name, String description) throws Exception;

    /**
     * deletes plant based on the plant_id
     *
     * @param plant_id      primary key of the plant in the database
     * @return              on success, returns deleted plant's plant_id, else throws exception
     * @throws SQLException if there is an error when executing SQL statements
     */
    public int deletePlant(int plant_id) throws Exception;

    /**
     * returns all of the plants in the database
     *
     * @return              list of Plants in the database.
     * @throws SQLException if there is an error when executing SQL statements
     */
    public List<Plant> getAllPlants() throws Exception;

}
