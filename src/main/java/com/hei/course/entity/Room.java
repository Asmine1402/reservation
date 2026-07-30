package com.hei.course.entity;

import lombok.*;

import java.util.List;
import java.util.UUID;
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
