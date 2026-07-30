package com.hei.course.entity;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Reservation {
private  UUID id;
private Instant createdAt;
private Projection projection;
private User user;
}
