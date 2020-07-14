USE oopp;

DROP TABLE IF EXISTS accounts;

CREATE TABLE accounts
(
    username  VARCHAR(45)   NOT NULL,
    password  VARCHAR(45)   NOT NULL,
    rating    DECIMAL(6, 2) NOT NULL,
    url       VARCHAR(45) NOT NULL
);
