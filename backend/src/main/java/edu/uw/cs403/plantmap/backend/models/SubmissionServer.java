package edu.uw.cs403.plantmap.backend.models;


import java.util.List;


/*
* SubmissionAccess accesses the submission data in database
*/
public interface SubmissionServer {
    /*
     * CreateSubmission create a new post
     */
    public int createSubmission(String posted_by, long post_date, int plant_id, float longitude, float latitude) throws Exception;

    /*
     * Create submission with images
     */
    public int createSubmission(String posted_by, long post_date, int plant_id, float longitude, float latitude, byte[] image) throws Exception;

    /*
     * DeleteSubmission deletes a post
     */
    public int deleteSubmission(int post_id) throws Exception;

    /*
     * */
    public Submission getSubmission(int post_id) throws Exception;

    /*
     * getAllSubmission gets all posts
     */
    public List<Submission> getAllSubmission() throws Exception;

    /*
     * Update submissions allows add or remove images in a post
     */
    public int updateSubmission(int post_id, byte[] image) throws Exception;
}
