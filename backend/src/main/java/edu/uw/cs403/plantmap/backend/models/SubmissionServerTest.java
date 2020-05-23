package edu.uw.cs403.plantmap.backend.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SubmissionServerTest implements SubmissionServer{

    HashMap<Integer, Submission> map = new HashMap<>();
    int index = 0;

    @Override
    public int createSubmission(String posted_by, long post_date, int plant_id, float longitude, float latitude) throws Exception {
        Submission sub = new Submission();
        sub.setPost_id(++index);
        sub.setPosted_by(posted_by);
        sub.setPost_date(post_date);
        sub.setPost_id(plant_id);
        sub.setLongitude(longitude);
        sub.setLatitude(latitude);
        map.put(index, sub);
        return index;
    }

    @Override
    public int createSubmission(String posted_by, long post_date, int plant_id, float longitude, float latitude, byte[] image) throws Exception {
        Submission sub = new Submission();
        sub.setPost_id(++index);
        sub.setPosted_by(posted_by);
        sub.setPost_date(post_date);
        sub.setPost_id(plant_id);
        sub.setLongitude(longitude);
        sub.setLatitude(latitude);
        sub.setImg(image);
        map.put(index, sub);
        return index;
    }

    @Override
    public int deleteSubmission(int post_id) throws Exception {
        if (!map.containsKey(post_id)) {
            throw new Exception("Post not exist");
        }
        map.remove(post_id);
        return post_id;
    }

    @Override
    public Submission getSubmission(int post_id) throws Exception {
        if (!map.containsKey(post_id)) {
            throw new Exception("Post not exist");
        }
        return map.get(post_id);
    }

    @Override
    public List<Submission> getAllSubmission(int thresh) throws Exception {
        return new ArrayList<>(map.values());
    }

    @Override
    public int updateSubmission(int post_id, byte[] image) throws Exception {
        if (!map.containsKey(post_id)) {
            throw new Exception("Post not exist");
        }
        map.get(post_id).setImg(image);
        return post_id;
    }

    @Override
    public byte[] getSubmissionImage(int post_id) throws Exception {
        return map.get(post_id).getImg();
    }

    @Override
    public int reportSubmission(int post_id) throws Exception {
        //TODO
        return 0;
    }

}
