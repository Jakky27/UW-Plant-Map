package edu.uw.cs403.plantmap.backend.models;

import edu.uw.cs403.plantmap.backend.SQLConnectionPool;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class PlantServerImp implements PlantServer {

    private SQLConnectionPool pool;

    public PlantServerImp(SQLConnectionPool pool) {
        this.pool = pool;
    }

    // SQL statements
    private static final String STATEMENT_INSERT = "INSERT INTO plant (name, description) VALUES (?, ?);";
    private static final String STATEMENT_READID = "SELECT * FROM plant WHERE plant_id = ?;";
    private static final String STATEMENT_READNAME = "SELECT * FROM plant WHERE name = ?;";
    private static final String STATEMENT_UPDATE = "Update plant SET name = ?, description = ? WHERE plant_id = ?";
    private static final String STATEMENT_DELETE = "DELETE FROM plant WHERE plant_id = ?";
    private static final String STATEMENT_GETALL = "SELECT * FROM plant";

    @Override
    public int registerPlant(String name, String description) throws Exception {
        Connection conn = null;

        try {
            conn = pool.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(STATEMENT_INSERT, Statement.RETURN_GENERATED_KEYS);
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
        } finally {
            if (conn != null) {
                pool.returnConnectionSafe(conn);
            }
        }
    }

    @Override
    public Plant getPlantById(int plant_id) throws Exception {
        Connection conn = null;

        try {
            conn = pool.getConnection();

            // run SQL
            PreparedStatement preparedStatement = conn.prepareStatement(STATEMENT_READID);
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
        } finally {
            if (conn != null) {
                pool.returnConnectionSafe(conn);
            }
        }
    }

    @Override
    public Plant getPlantByName(String name) throws Exception {
        Connection conn = null;

        try {
            conn = pool.getConnection();

            // run SQL
            PreparedStatement preparedStatement = conn.prepareStatement(STATEMENT_READNAME);
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
        } finally {
            if (conn != null) {
                pool.returnConnectionSafe(conn);
            }
        }
    }

    @Override
    public int updatePlant(int plant_id, String name, String description) throws Exception {
        Connection conn = null;

        try {
            conn = pool.getConnection();

            // get plant from db
            Plant plant = getPlantByName(name);

            // run SQL
            PreparedStatement preparedStatement = conn.prepareStatement(STATEMENT_UPDATE);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setInt(3,plant_id);

            preparedStatement.executeUpdate();

            return plant_id;

        } catch (SQLException  e){
            throw new SQLException("Encountered an error when executing given sql statement.", e);
        } finally {
            if (conn != null) {
                pool.returnConnectionSafe(conn);
            }
        }
    }

    @Override
    public int deletePlant(int plant_id) throws Exception {
        Connection conn = null;

        try {
            conn = pool.getConnection();

            // run SQL
            PreparedStatement preparedStatement = conn.prepareStatement(STATEMENT_DELETE);
            preparedStatement.setInt(1,plant_id);
            preparedStatement.executeUpdate();

            return plant_id;

        } catch (SQLException  e){
            throw new SQLException("Encountered an error when executing given sql statement.", e);
        } finally {
            if (conn != null) {
                pool.returnConnectionSafe(conn);
            }
        }
    }

    @Override
    public List<Plant> getAllPlants() throws Exception {
        Connection conn = null;

        try {
            conn = pool.getConnection();

            Statement statement = conn.createStatement();
            ResultSet results = statement.executeQuery(STATEMENT_GETALL);
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
        } finally {
            if (conn != null) {
                pool.returnConnectionSafe(conn);
            }
        }
    }
}
