package edu.uw.cs403.plantmap.backend.models;

import com.microsoft.sqlserver.jdbc.Geography;

import java.sql.Blob;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

/*
* SubmissionAccess accesses the submission data in database
*/
public interface SubmissionServer {
    /*
     * CreateSubmission create a new post
     */
    public void createSubmission(String posted_by, Date post_date, int plant_id, float longitude, float latitude) throws Exception;

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
}
