package com.hei.course.entity;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class BookReservation {
    private UUID id;
    private Seat seat;
    private Reservation reservation;
}
