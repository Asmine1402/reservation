package com.hei.course.model;

import java.math.BigDecimal;
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
public class Projection {
  private UUID id;
  private Instant datetime;
  private BigDecimal seatPrice;
  private Movie movie;
  private Room room;
}
