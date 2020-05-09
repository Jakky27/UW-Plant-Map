package edu.uw.cs403.plantmap.backend.models;

import lombok.Data;

import java.util.UUID;

@Data
public class Plants {
    private UUID plant_id;
    private String name;
    private String description;
}
