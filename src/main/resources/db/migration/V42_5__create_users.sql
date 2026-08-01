CREATE TABLE users
(
    id        UUID PRIMARY KEY,
    firstname VARCHAR(255) NOT NULL,
    lastname  VARCHAR(255) NOT NULL,
    birthdate TIMESTAMP    NOT NULL,
    email     VARCHAR(255) NOT NULL,
    phone     VARCHAR(20),
    password  VARCHAR(255) NOT NULL,
    user_role user_role    NOT NULL,
    CONSTRAINT uq_users_email UNIQUE (email)
);