--clean DB
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS holdings;
DROP TABLE IF EXISTS users;


--create users table
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    balance DECIMAL(18, 6) NOT NULL
);
--create transactions table
CREATE TABLE IF NOT EXISTS transactions(
    id SERIAL PRIMARY KEY,
        user_id BIGINT NOT NULL,
        crypto_ticker VARCHAR(10) NOT NULL,
        quantity DECIMAL(18, 6) NOT NULL,
        price DECIMAL(18, 6) NOT NULL,
        transaction_type VARCHAR(10) NOT NULL,
        timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY (user_id) REFERENCES users(id)
);
--create holdings table
CREATE TABLE holdings (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    crypto_ticker VARCHAR(10) NOT NULL,
    quantity DECIMAL(18, 6) NOT NULL,
    CONSTRAINT unique_user_crypto UNIQUE (user_id, crypto_ticker),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
--insert test user with default 10000
INSERT INTO users (balance) VALUES (10000.000000);
