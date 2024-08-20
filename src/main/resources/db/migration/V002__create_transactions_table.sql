CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    type VARCHAR NOT NULL,
    amount INT NOT NULL,
    date VARCHAR NOT NULL,
    source_account_id VARCHAR NOT NULL,
    target_account_id VARCHAR,
    CONSTRAINT fk_account_source
          FOREIGN KEY(source_account_id)
            REFERENCES accounts(account_id)
);
