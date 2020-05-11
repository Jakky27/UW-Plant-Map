package edu.uw.cs403.plantmap.backend.models;

import com.microsoft.sqlserver.jdbc.Geography;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubmissionServerImp implements SubmissionServer {

    private Connection conn;

    public SubmissionServerImp(Connection conn) {
        this.conn = conn;
    }

    // SQL statements
    private static String insertStatement = "INSERT INTO submission (postedBy, post_date, plant_id, image, geoLocation) VALUES (?, ?, ?, ?, ?);";
    private static String readStatement = "SELECT * FROM submission WHERE post_id = ?;";
    private static String deleteStatement = "DELETE FROM submission WHERE post_id = ?";
    private static String getAllStatement = "SELECT * FROM submission";

    @Override
    public void createSubmission(String postedBy, Date post_date, int plant_id, Blob image, Geography geoLocation) throws Exception {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertStatement);
            preparedStatement.setString(1,postedBy);
            preparedStatement.setDate(2,post_date);
            preparedStatement.setInt(3,plant_id);
            preparedStatement.setBytes(4,geoLocation.serialize());
            preparedStatement.executeUpdate();
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

            sub.setPost_id(results.getInt(1));
            sub.setPostedBy(results.getString(2));
            sub.setPost_date(results.getDate(3));
            sub.setImage(results.getBlob(4));
            sub.setGeoLocation(Geography.deserialize(results.getBytes(5)));

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

                sub.setPost_id(results.getInt(1));
                sub.setPostedBy(results.getString(2));
                sub.setPost_date(results.getDate(3));
                sub.setImage(results.getBlob(4));
                sub.setGeoLocation(Geography.deserialize(results.getBytes(5)));
                postList.add(sub);
            }

            return postList;

        } catch (SQLException  e){
            throw new SQLException("Encountered an error when executing given sql statement.", e);
        }
    }
}
