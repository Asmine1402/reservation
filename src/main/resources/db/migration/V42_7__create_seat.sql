CREATE TABLE seats
(
    id      UUID PRIMARY KEY,
    number  VARCHAR(50) NOT NULL,
    room_id UUID        NOT NULL,
    CONSTRAINT fk_seats_room FOREIGN KEY (room_id) REFERENCES rooms (id)
);