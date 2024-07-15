
INSERT INTO ClientOrg (name, registered_date, expiry_date)
VALUES
    ('Client 1', '2023-01-01', '2024-12-31'),
    ('Client 2', '2023-02-01', '2025-01-31');

-- Insert initial data into Personnel
INSERT INTO Personnel (username, first_name, last_name, password, email, phone_number, client_id)
VALUES
    ('user1', 'John', 'Doe', 'password1', 'john.doe@example.com', '1234567890', 1),
    ('user2', 'Jane', 'Smith', 'password2', 'jane.smith@example.com', '9876543210', 2);
