package com.hei.course.model;

import java.time.Instant;
import java.util.UUID;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class Reservation {
  private UUID id;
  private Instant createdAt;
  private Projection projection;
  private User user;
}
