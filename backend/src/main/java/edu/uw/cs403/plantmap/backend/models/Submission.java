package edu.uw.cs403.plantmap.backend.models;

import lombok.Data;

import java.sql.Blob;
import java.sql.Date;
import java.util.UUID;
import com.microsoft.sqlserver.jdbc.Geography;

@Data
public class Submission {

    private UUID post_id;
    private String postedBy;
    private Date post_date;
    private Plant plant;
    private Blob image;
    private Geography geoLocation; //Not sure about the data type

}


