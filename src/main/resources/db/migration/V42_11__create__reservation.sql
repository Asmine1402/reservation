CREATE TABLE reservations
(
    id            UUID        PRIMARY KEY,
    created_at    TIMESTAMPTZ NOT NULL,
    projection_id UUID        NOT NULL,
    user_id       UUID        NOT NULL,
    CONSTRAINT fk_reservations_projection FOREIGN KEY (projection_id) REFERENCES projections (id),
    CONSTRAINT fk_reservations_user FOREIGN KEY (user_id) REFERENCES users (id)
);