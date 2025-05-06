--insert test user with default 10000
INSERT INTO users (balance) VALUES (10000.000000);
--insert some holdings for testing
INSERT INTO holdings (user_id, crypto_ticker, quantity)
            VALUES
            (1, 'BTC', 0.075423),
            (1, 'ETH', 1.235670);