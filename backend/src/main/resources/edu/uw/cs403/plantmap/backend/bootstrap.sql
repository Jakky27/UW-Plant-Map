CREATE TABLE plant (
    plant_id INT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(30),
    description TEXT
);

CREATE TABLE submission (
    post_id INT IDENTITY(1,1) PRIMARY KEY,
    plant_id INT FOREIGN KEY REFERENCES plant(plant_id),
    latitude FLOAT,
    longitude FLOAT,
    posted_by VARCHAR(30),
    post_date BIGINT,
    img VARBINARY
);