CREATE TABLE book_reservations
(
    id             UUID PRIMARY KEY,
    seat_id        UUID NOT NULL,
    reservation_id UUID NOT NULL,
    CONSTRAINT fk_book_reservations_seat FOREIGN KEY (seat_id) REFERENCES seats (id),
    CONSTRAINT fk_book_reservations_reservation FOREIGN KEY (reservation_id) REFERENCES reservations (id),
    CONSTRAINT uq_book_reservations_seat_reservation UNIQUE (seat_id, reservation_id)
);