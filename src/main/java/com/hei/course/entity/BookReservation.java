package com.hei.course.entity;

import java.util.UUID;
import lombok.*;

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
