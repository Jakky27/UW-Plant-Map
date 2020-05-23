package edu.uw.cs403.plantmap.backend.models;

import edu.uw.cs403.plantmap.backend.SQLConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubmissionServerImp implements SubmissionServer {

    private SQLConnectionPool pool;

    public SubmissionServerImp(SQLConnectionPool pool) {
        this.pool = pool;
    }

    // SQL statements
    private static final String STATEMENT_INSERT = "INSERT INTO submission (posted_by, post_date, plant_id, longitude, latitude) VALUES (?, ?, ?, ?, ?);";
    private static final String STATEMENT_INSERT_WITH_IMAGE = "INSERT INTO submission (posted_by, post_date, plant_id, longitude, latitude, img) VALUES (?, ?, ?, ?, ?, ?);";
    private static final String STATEMENT_READ = "SELECT posted_by, post_date, plant_id, longitude, latitude FROM submission WHERE post_id = ?;";
    private static final String STATEMENT_READIMG = "SELECT img FROM submission WHERE post_id = ?;";
    private static final String STATEMENT_DELETE = "DELETE FROM submission WHERE post_id = ?";
    private static final String STATEMENT_GETALL = "SELECT posted_by, post_date, plant_id, longitude, latitude, post_id FROM submission";
    private static final String STATEMENT_UPDATE = "UPDATE submission SET img = ? WHERE post_id = ?";

    @Override
    public int createSubmission(String posted_by, long post_date, int plant_id, float longitude, float latitude) throws Exception {
        Connection conn = null;

        try {
            conn = pool.getConnection();

            PreparedStatement preparedStatement = conn.prepareStatement(STATEMENT_INSERT, Statement.RETURN_GENERATED_KEYS);
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
        } finally {
            if (conn != null) {
                pool.returnConnection(conn);
            }
        }
    }

    @Override
    public int createSubmission(String posted_by, long post_date, int plant_id, float longitude, float latitude, byte[] image) throws Exception {
        Connection conn = null;

        try {
            conn = pool.getConnection();

            PreparedStatement preparedStatement = conn.prepareStatement(STATEMENT_INSERT_WITH_IMAGE, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,posted_by);
            preparedStatement.setLong(2,post_date);
            preparedStatement.setInt(3,plant_id);
            preparedStatement.setFloat(4, longitude);
            preparedStatement.setFloat(5,latitude);
            preparedStatement.setBytes(6, image);
            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()){
                return rs.getInt(1);
            }else{
                return 0;
            }

        } catch (SQLException e){
            throw new SQLException("Encountered an error when executing given sql statement.", e);
        } finally {
            if (conn != null) {
                pool.returnConnection(conn);
            }
        }
    }

    @Override
    public int deleteSubmission(int post_id) throws Exception {
        Connection conn = null;

        try {
            conn = pool.getConnection();

            // run SQL
            PreparedStatement preparedStatement = conn.prepareStatement(STATEMENT_DELETE);
            preparedStatement.setInt(1,post_id);
            preparedStatement.executeUpdate();

            return post_id;

        } catch (SQLException  e){
            throw new SQLException("Encountered an error when executing given sql statement.", e);
        } finally {
            if (conn != null) {
                pool.returnConnection(conn);
            }
        }
    }

    @Override
    public Submission getSubmission(int post_id) throws Exception {
        Connection conn = null;

        try {
            conn = pool.getConnection();

            // run SQL
            PreparedStatement preparedStatement = conn.prepareStatement(STATEMENT_READ);
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
        } finally {
            if (conn != null) {
                pool.returnConnection(conn);
            }
        }
    }

    @Override
    public List<Submission> getAllSubmission() throws Exception {
        Connection conn = null;

        try {
            conn = pool.getConnection();

            // run SQL
            Statement statement = conn.createStatement();
            ResultSet results = statement.executeQuery(STATEMENT_GETALL);
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
        } finally {
            if (conn != null) {
                pool.returnConnection(conn);
            }
        }
    }

    @Override
    public int updateSubmission(int post_id, byte[] image) throws Exception {
        Connection conn = null;

        try {
            conn = pool.getConnection();

            // run SQL
            PreparedStatement preparedStatement = conn.prepareStatement(STATEMENT_UPDATE);
            preparedStatement.setBytes(1, image);
            preparedStatement.setInt(2,post_id);
            preparedStatement.executeUpdate();

           return post_id;

        } catch (SQLException  e){
            throw new SQLException("Encountered an error when executing given sql statement.", e);
        } finally {
            if (conn != null) {
                pool.returnConnection(conn);
            }
        }
    }

    @Override
    public byte[] getSubmissionImage(int post_id) throws Exception {
        Connection conn = null;

        try {
            conn = pool.getConnection();

            // run SQL
            PreparedStatement preparedStatement = conn.prepareStatement(STATEMENT_READIMG);
            preparedStatement.setInt(1,post_id);
            ResultSet results = preparedStatement.executeQuery();

            results.next();
            return results.getBytes("img");
        } catch (SQLException  e){
            throw new SQLException("Encountered an error when executing given sql statement.", e);
        } finally {
            if (conn != null) {
                pool.returnConnection(conn);
            }
        }
    }
}
