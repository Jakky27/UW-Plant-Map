package edu.uw.cs403.plantmap.backend.models;


import java.util.List;


/*
* SubmissionAccess accesses the submission data in database
*/
public interface SubmissionServer {
    /**
     * Creates a new submission in the database based on the given information
     *
     * @param posted_by name of user who posted submission
     * @param post_date date of post
     * @param plant_id  foreign key of plant in the database
     * @param longitude longitude of location as a float
     * @param latitude  latitude of location as a float
     * @return          on success, returns primary key of new submission, else 0
     * @throws SQLException if there is an error when executing SQL statements
     */
    public int createSubmission(String posted_by, long post_date, int plant_id, float longitude, float latitude) throws Exception;

    /*
     * Create submission with images
     */
    public int createSubmission(String posted_by, long post_date, int plant_id, float longitude, float latitude, byte[] image) throws Exception;

    /**
     * Deletes post with given id from the database
     *
     * @param post_id   primary key passed to determine which post to delete
     * @return          on success returns key, else throws exception
     * @throws SQLException if there is an error when executing SQL statements
     */
    public int deleteSubmission(int post_id) throws Exception;

    /**
     * retrieves submission based on post_id.
     *
     * @param post_id       primary key of the post to be retrieved
     * @return              submission that was retrieved
     * @throws SQLException if there is an error when executing SQL statements
     */
    public Submission getSubmission(int post_id) throws Exception;

    /**
     * returns a list of all of the valid submissions to fill the user feed
     *
     * @param  thresh       specifies the highest number of reports deemed acceptable for a submission to
     *                      be included.
     * @return              list of all of the submissions to be returned
     * @throws SQLException if there is an error when executing SQL statements
     */
    public List<Submission> getAllSubmission(int thresh) throws Exception;

    /*
     * Update submissions allows add or remove images in a post
     */
    public int updateSubmission(int post_id, byte[] image) throws Exception;

    byte[] getSubmissionImage(int post_id) throws Exception;
}
