package com.hei.course.model;

import java.util.List;
import java.util.UUID;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Room {
  private UUID id;
  private String number;
  private int capacity;
  List<Seat> seatList;
  List<Projection> projectionList;
}
