CREATE TABLE rooms
(
    id       UUID PRIMARY KEY,
    number   VARCHAR(50) NOT NULL,
    capacity INT         NOT NULL
);