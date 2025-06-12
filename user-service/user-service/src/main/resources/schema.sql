DROP TABLE IF EXISTS users;


--create users table
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    balance DECIMAL(18, 6) NOT NULL
);