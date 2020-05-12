package edu.uw.cs403.plantmap.backend.models;

import lombok.Data;

@Data
public class Plant {
    private int plant_id;
    private String name;
    private String description;
}
