
CREATE TABLE ClientOrg (
    client_id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    registered_date DATE NOT NULL,
    expiry_date DATE NOT NULL,
    enabled BOOLEAN NOT NULL
);

CREATE TABLE Personnel (
    personnel_id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    client_id INT,
    FOREIGN KEY (client_id) REFERENCES ClientOrg(client_id)
);
