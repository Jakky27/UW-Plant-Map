package edu.uw.cs403.plantmap.backend.models;

import lombok.Data;

import java.sql.Date;
import java.util.UUID;

@Data
public class Submission {
    private UUID post_id;
    private String postedBy;
    private Date post_date;
    private Plants plant;

    //TODO: not sure about the type
    private String image;
    private String geoLocation;

}
