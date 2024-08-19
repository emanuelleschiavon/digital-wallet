CREATE TABLE wallets(
    id SERIAL PRIMARY KEY,
    account_id VARCHAR NOT NULL,
    balance INT NOT NULL
);
