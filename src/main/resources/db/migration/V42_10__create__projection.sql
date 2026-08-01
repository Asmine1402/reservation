CREATE TABLE projections
(
    id         UUID           PRIMARY KEY,
    datetime   TIMESTAMPTZ    NOT NULL,
    seat_price NUMERIC(10, 2) NOT NULL,
    movie_id   UUID           NOT NULL,
    room_id    UUID           NOT NULL,
    CONSTRAINT fk_projections_movie FOREIGN KEY (movie_id) REFERENCES movies (id),
    CONSTRAINT fk_projections_room FOREIGN KEY (room_id) REFERENCES rooms (id)
);