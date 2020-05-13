package edu.uw.cs403.plantmap.backend.models;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class PlantServerImp implements PlantServer {

    private Connection conn;

    public PlantServerImp(Connection conn) {
        this.conn = conn;
    }

    // SQL statements
    private static String insertStatement = "INSERT INTO plant (name, description) VALUES (?, ?);";
    private static String readIdStatement = "SELECT * FROM plant WHERE plant_id = ?;";
    private static String readNameStatement = "SELECT * FROM plant WHERE name = ?;";
    private static String updateStatement = "Update plant SET name = ?, description = ? WHERE plant_id = ?";
    private static String deleteStatement = "DELETE FROM plant WHERE plant_id = ?";
    private static String getAllStatement = "SELECT * FROM plant";

    @Override
    public int registerPlant(String name, String description) throws Exception {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertStatement);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,description);
            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()){
                return rs.getInt(1);
            }else {
                return 0;
            }

        } catch (SQLException  e){
            throw new SQLException("Encountered an error when executing given sql statement.", e);
        }
    }

    @Override
    public Plant getPlantById(int plant_id) throws Exception {
        try {
            // run SQL
            PreparedStatement preparedStatement = conn.prepareStatement(readIdStatement);
            preparedStatement.setInt(1,plant_id);
            ResultSet results = preparedStatement.executeQuery();

            // form new Plant object from result set
            Plant plant = new Plant();
            results.next();
            plant.setPlant_id(results.getInt(1));
            plant.setName(results.getString(2));
            plant.setDescription(results.getString(3));

            return plant;

        } catch (SQLException  e){
            throw new SQLException("Encountered an error when executing given sql statement.", e);
        }
    }

    @Override
    public Plant getPlantByName(String name) throws Exception {
        try {
            // run SQL
            PreparedStatement preparedStatement = conn.prepareStatement(readNameStatement);
            preparedStatement.setString(1,name);
            ResultSet results = preparedStatement.executeQuery();

            // form new Plant object from result set
            Plant plant = new Plant();
            results.next();
            plant.setPlant_id(results.getInt(1));
            plant.setName(results.getString(2));
            plant.setDescription(results.getString(3));

            return plant;

        } catch (SQLException e){
            throw new SQLException("Encountered an error when executing given sql statement.", e);
        }
    }

    @Override
    public int updatePlant(int plant_id, String name, String description) throws Exception {
        try {
            // get plant from db
            Plant plant = getPlantByName(name);

            // run SQL
            PreparedStatement preparedStatement = conn.prepareStatement(updateStatement);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setInt(3,plant_id);

            preparedStatement.executeUpdate();

            return plant_id;

        } catch (SQLException  e){
            throw new SQLException("Encountered an error when executing given sql statement.", e);
        }
    }

    @Override
    public int deletePlant(int plant_id) throws Exception {
        try {
            // run SQL
            PreparedStatement preparedStatement = conn.prepareStatement(deleteStatement);
            preparedStatement.setInt(1,plant_id);
            preparedStatement.executeUpdate();

            return plant_id;

        } catch (SQLException  e){
            throw new SQLException("Encountered an error when executing given sql statement.", e);
        }
    }

    @Override
    public List<Plant> getAllPlants() throws Exception {
        try {
            Statement statement = conn.createStatement();
            ResultSet results = statement.executeQuery(getAllStatement);
            List<Plant> plantList = new ArrayList<>();
            while (results.next()){
                Plant plant = new Plant();
                plant.setPlant_id(results.getInt(1));
                plant.setName(results.getString(2));
                plant.setDescription(results.getString(3));
                plantList.add(plant);
            }

            return plantList;
        } catch (SQLException  e){
            throw new SQLException("Encountered an error when executing given sql statement.", e);
        }
    }
}
