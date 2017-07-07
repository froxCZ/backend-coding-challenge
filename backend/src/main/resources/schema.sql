CREATE TABLE IF NOT EXISTS expense
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    amount DECIMAL(19,2) NOT NULL,
    date DATE,
    exchange_rate DECIMAL(19,4) NOT NULL,
    original_currency VARCHAR(5),
    reason LONGTEXT,
    vat DECIMAL(19,2) NOT NULL
);