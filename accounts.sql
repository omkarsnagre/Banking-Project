CREATE TABLE accounts (
    accNumber BIGINT NOT NULL PRIMARY KEY,    -- Primary key for account number
    fullName VARCHAR(255) NOT NULL,           -- Full name of the account holder
    email VARCHAR(255) NOT NULL UNIQUE,       -- Unique email for the account holder
    balance DECIMAL(10,2) NOT NULL,           -- Account balance with 2 decimal precision
    securityPin CHAR(6) NOT NULL              -- Security PIN with a fixed length of 6 characters
);
