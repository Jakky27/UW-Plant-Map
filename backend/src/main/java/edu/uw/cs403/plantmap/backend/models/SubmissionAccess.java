package edu.uw.cs403.plantmap.backend.models;

import com.microsoft.sqlserver.jdbc.Geography;

import java.sql.Blob;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

/*
* SubmissionAccess accesses the submission data in database
*/
public interface SubmissionAccess {
    /*
     * CreateSubmission create a new post
     */
    public UUID createSubmission(String postedBy, Date post_date, Plant plant, Blob image, Geography geoLocation) throws Exception;

    /*
     * DeleteSubmission deletes a post
     */
    public UUID deleteSubmission(UUID post_id) throws Exception;

    /*
     * */
    public Submission getSubmission(UUID post_id) throws Exception;

    /*
     * getAllSubmission gets all posts
     */
    public List<Submission> getAllSubmission() throws Exception;
}
