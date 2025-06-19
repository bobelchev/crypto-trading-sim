--clean DB
DROP TABLE IF EXISTS transactions;



--create transactions table
CREATE TABLE IF NOT EXISTS transactions(
    id SERIAL PRIMARY KEY,
        user_id BIGINT NOT NULL,
        crypto_ticker VARCHAR(10) NOT NULL,
        quantity DECIMAL(18, 6) NOT NULL,
        price DECIMAL(18, 6) NOT NULL,
        transaction_type VARCHAR(10) NOT NULL,
        timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        profit_loss DECIMAL(18, 6) NOT NULL
);

