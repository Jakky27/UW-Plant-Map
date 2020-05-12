package edu.uw.cs403.plantmap.backend.models;

import lombok.Data;

import java.sql.Blob;
import java.sql.Date;
import com.microsoft.sqlserver.jdbc.Geography;

@Data
public class Submission {

    private int post_id;
    private String postedBy;
    private Date post_date;
    private int plant_id;
    private Blob image; // Need further check
    private float longitude;
    private float latitude;

}


