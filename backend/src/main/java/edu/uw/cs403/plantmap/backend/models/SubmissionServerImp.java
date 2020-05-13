package edu.uw.cs403.plantmap.backend.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubmissionServerImp implements SubmissionServer {

    private Connection conn;

    public SubmissionServerImp(Connection conn) {
        this.conn = conn;
    }

    // SQL statements
    private static String insertStatement = "INSERT INTO submission (posted_by, post_date, plant_id, longitude, latitude) VALUES (?, ?, ?, ?, ?);";
    private static String readStatement = "SELECT posted_by, post_date, plant_id, longitude, latitude FROM submission WHERE post_id = ?;";
    private static String deleteStatement = "DELETE FROM submission WHERE post_id = ?";
    private static String getAllStatement = "SELECT posted_by, post_date, plant_id, longitude, latitude, post_id FROM submission";

    @Override
    public int createSubmission(String posted_by, long post_date, int plant_id, float longitude, float latitude) throws Exception {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,posted_by);
            preparedStatement.setLong(2,post_date);
            preparedStatement.setInt(3,plant_id);
            preparedStatement.setFloat(4, longitude);
            preparedStatement.setFloat(5,latitude);
            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()){
                return rs.getInt(1);
            }else{
                return 0;
            }

        } catch (SQLException e){
            throw new SQLException("Encountered an error when executing given sql statement.", e);
        }
    }

    @Override
    public int deleteSubmission(int post_id) throws Exception {
        try {
            // run SQL
            PreparedStatement preparedStatement = conn.prepareStatement(deleteStatement);
            preparedStatement.setInt(1,post_id);
            preparedStatement.executeUpdate();

            return post_id;

        } catch (SQLException  e){
            throw new SQLException("Encountered an error when executing given sql statement.", e);
        }
    }

    @Override
    public Submission getSubmission(int post_id) throws Exception {
        try {
            // run SQL
            PreparedStatement preparedStatement = conn.prepareStatement(readStatement);
            preparedStatement.setInt(1,post_id);
            ResultSet results = preparedStatement.executeQuery();

            // form new Plant object from result set
            Submission sub = new Submission();
            results.next();

            // (posted_by, post_date, plant_id, longitude, latitude)
            sub.setPost_id(post_id);
            sub.setPosted_by(results.getString(1));
            sub.setPost_date(results.getLong(2));
            sub.setPlant_id(results.getInt(3));
            sub.setLongitude(results.getFloat(4));
            sub.setLatitude(results.getFloat(5));

            return sub;

        } catch (SQLException  e){
            throw new SQLException("Encountered an error when executing given sql statement.", e);
        }
    }

    @Override
    public List<Submission> getAllSubmission() throws Exception {
        try {
            // run SQL
            Statement statement = conn.createStatement();
            ResultSet results = statement.executeQuery(getAllStatement);
            List<Submission> postList = new ArrayList<>();

            // form new Plant object from result set
            while (results.next()){
                Submission sub = new Submission();

                // (posted_by, post_date, plant_id, longitude, latitude)
                sub.setPosted_by(results.getString(1));
                sub.setPost_date(results.getLong(2));
                sub.setPlant_id(results.getInt(3));
                sub.setLongitude(results.getFloat(4));
                sub.setLatitude(results.getFloat(5));
                sub.setPost_id(results.getInt(6));

                postList.add(sub);
            }

            return postList;

        } catch (SQLException  e){
            throw new SQLException("Encountered an error when executing given sql statement.", e);
        }
    }
}
