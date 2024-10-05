CREATE TABLE user (
    fullName VARCHAR(255) NOT NULL,           -- Full name of the user
    email VARCHAR(255) NOT NULL PRIMARY KEY,  -- Primary key is the user's email
    password VARCHAR(255) NOT NULL            -- Password for authentication
);
