CREATE TABLE movies
(
    id          UUID         PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    duration    NUMERIC(21)  NOT NULL
);