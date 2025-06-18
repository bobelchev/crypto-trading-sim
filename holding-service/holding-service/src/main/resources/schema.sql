--clean DB
DROP TABLE IF EXISTS holdings;
-- Create holdings table only
CREATE TABLE IF NOT EXISTS holdings (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    crypto_ticker VARCHAR(10) NOT NULL,
    quantity DECIMAL(18, 6) NOT NULL,
    average_price DECIMAL(18, 6) NOT NULL,
    CONSTRAINT unique_user_crypto UNIQUE (user_id, crypto_ticker)
);

